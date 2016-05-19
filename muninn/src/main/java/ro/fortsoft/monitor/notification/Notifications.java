package ro.fortsoft.monitor.notification;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ro.fortsoft.monitor.alert.AlertingGateway;
import ro.fortsoft.monitor.elasticsearch.response.extractor.function.json.Json;
import ro.fortsoft.monitor.event.Event;
import ro.fortsoft.monitor.event.EventGroup;
import ro.fortsoft.monitor.event.EventsHolder;
import ro.fortsoft.monitor.notification.entity.SentAlerts;
import ro.fortsoft.monitor.rule.base.BaseRule;
import ro.fortsoft.monitor.rule.processor.context.ExecutionContext;
import ro.fortsoft.monitor.storage.Storage;
import ro.fortsoft.monitor.util.json.JsonSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Serban Balamaci
 */
public class Notifications implements ApplicationContextAware {

    @Autowired
    protected AlertingGateway alertingGateway;

    @Autowired
    protected JsonSerializer jsonSerializer;

    private ApplicationContext applicationContext;

    public void notify(BaseRule rule, ExecutionContext executionContext, EventsHolder events) {
        EventGroup eventGroup = new EventGroup(executionContext, events);

        List<EventGroup> eventGroupList = Lists.newArrayList(eventGroup);

        List<SentAlerts> sentAlerts = getPreviousSentAlerts(rule);
        eventGroupList = filterEventsForAlreadySentAlerts(sentAlerts, eventGroupList);

        alertingGateway.alert(rule, eventGroupList);

        registerNewSentAlerts(rule, eventGroupList, sentAlerts);
    }

    private List<SentAlerts> getPreviousSentAlerts(BaseRule rule) {
        if(rule.getRealertFrequency() != null) {
            Optional<JsonElement> alerts = getStorage(rule).load(rule.getId(), Storage.Key.ALERTS.name());
            if(! alerts.isPresent()) {
                return Collections.emptyList();
            }

            SentAlerts[] sentAlerts = jsonSerializer.
                    fromJson(alerts.get().toString(), SentAlerts[].class);
            List<SentAlerts> sentAlertsList = Arrays.asList(sentAlerts);

            return filterExpiredSentAlerts(rule.getRealertFrequency(), sentAlertsList);
        }
        return Collections.emptyList();
    }

    private void registerNewSentAlerts(BaseRule rule, List<EventGroup> eventGroupList,
                                      List<SentAlerts> previousSentAlertsList) {
        if(rule.getRealertFrequency() != null) {
            List<SentAlerts> sentAlerts = new ArrayList<>(previousSentAlertsList);

            Set<String> newAlertedEventIds = eventsToSentAlertIds(eventGroupList);
            SentAlerts newSentAlerts = new SentAlerts(Instant.now(), newAlertedEventIds);
            sentAlerts.add(newSentAlerts);

            getStorage(rule).save(rule.getId(), Storage.Key.ALERTS.name(),
                    Json.fromString(jsonSerializer.toJson(sentAlerts)));
        }
    }

    /**
     * Filters the old SentAlerts for alerts that are expired
     * @param realertFrequency - frequency to determined the alerts have expired
     * @param alertedEvents - the events for which we alerted already
     * @return list of events that are not expired
     */
    private List<SentAlerts> filterExpiredSentAlerts(Duration realertFrequency, List<SentAlerts> alertedEvents) {
        Instant expiredAlertTime = Instant.now().minus(realertFrequency);

        return alertedEvents.stream()
                .filter(ev -> ev.getTime().isAfter(expiredAlertTime)).
                        collect(Collectors.toList());
    }

    /**
     * We filter out the events from the list for which we already sent the alerts
     * @param ruleSentAlerts sent alerts
     * @param eventGroups
     * @return EventGroup with events filtered
     */
    private List<EventGroup> filterEventsForAlreadySentAlerts(List<SentAlerts> ruleSentAlerts,
                                                              List<EventGroup> eventGroups) {

        final Set<String> previousAlertIds = ruleSentAlerts.stream().
                    map(SentAlerts::getIds).flatMap(Collection::stream).collect(Collectors.toSet());

        List<EventGroup> filteredEventGroup = new ArrayList<>();

        for(EventGroup group : eventGroups) {
            Stream<? extends Event> eventsStream = group.getEventsHolder().getEvents().stream();
            List<? extends Event> events = eventsStream
                    .filter(eventWithNoFilterKey().or(eventAlertNotAlreadySent(previousAlertIds)))
                    .collect(Collectors.toList());

            EventsHolder eventsHolder = new EventsHolder(group.getEventsHolder().getMetadata(), events);

            EventGroup filteredEvent = new EventGroup(group.getKey(), eventsHolder);
            filteredEventGroup.add(filteredEvent);
        }

        return filteredEventGroup;
    }

    private Predicate<Event> eventWithNoFilterKey() {
        return eventWithFilterKey().negate();
    }
    private Predicate<Event> eventWithFilterKey() {
        return ev -> ev.getFilterKey().isPresent();
    }

    private Predicate<Event> eventAlertNotAlreadySent(final Set<String> previousAlertIds) {
        return ev -> ev.getFilterKey().isPresent() && !previousAlertIds.contains(ev.getFilterKey().get());
    }

    private Set<String> eventsToSentAlertIds(List<EventGroup> eventGroups) {
        Set<String> alertIds = new HashSet<>();

        for(EventGroup group : eventGroups) {
            Set<String> events = group.getEventsHolder().getEvents().stream()
                    .filter(eventWithFilterKey())
                    .map(ev -> (String) ev.getFilterKey().get())
                    .collect(Collectors.toSet());
            alertIds.addAll(events);
        }

        return alertIds;
    }

    private Storage getStorage(BaseRule rule) {
        String storageBeanName = rule.getConfig().getString("storage");
        return (Storage) applicationContext.getBean("storage." + storageBeanName, rule.getConfig());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
