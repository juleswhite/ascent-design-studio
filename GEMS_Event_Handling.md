# Introduction #

If you use ClientModelObjects as your client-side model, GEMS provides a set of built-in event handling classes. This page introduces you to these classes and how to use them.


# Listening for Changes on a ClientModelObject #

To listen for changes on a specific ClientModelObject instance, you must register an instance of a org.gems.ajax.client.model.event.ModelListener with that instance. The ModelListener has a few simple methods:

```
public interface ModelListener {
        /**
	 * Called after a parent has a new child added.
	 * @param evt
	 */
	public void childAdded(ContainmentEvent evt);
	
	/**
	 * Called after a parent has a child removed.
	 * @param evt
	 */
	public void childRemoved(ContainmentEvent evt);
	
	/**
	 * Called after a connection is added to an element.
	 * This method is dispatched once for both the source and target
	 * of the association.
	 * @param evt
	 */
	public void connectionAdded(ConnectionEvent evt);
	
	/**
	 * Called after a connection is removed from an element.
	 * This method is dispatched once for both the source and target
	 * of the association.
	 * @param evt
	 */
	public void connectionRemoved(ConnectionEvent evt);
	
	/**
	 * Called after a property has changed on an element.
	 * @param evt
	 */
	public void propertyChanged(PropertyEvent evt);
}

```

To add a listener to a ClientModelObject instance:
```
ClientModelObject obj = ...;
ModelListener mylistener = ...;

//add the listener
obj.addModelListener(mylistener);
```

In some cases, you may want to receive notification of changes to a model before they are executed. For example, to write your own custom constraint checker, you might want to be able to intercept and veto events that would violate the model constraints. To intercept and possibly veto events before they are applied, create an instance of the org.gems.ajax.client.model.event.ProposedChangeListener interface. The ProposedChangeListener has methods that are called before events are applied to the model (when they can be vetoed):

```
public interface ProposedChangeListener extends ModelListener {

	/**
	 * Called before a parent has a new child added.
	 * @param evt
	 */
	public void aboutToAddChild(ContainmentEvent evt);
	
	/**
	 * Called before a parent has a child removed.
	 * @param evt
	 */
	public void aboutToRemoveChild(ContainmentEvent evt);
	
	/**
	 * Called before a connection is added to an element.
	 * This method is dispatched once for both the source and target
	 * of the association.
	 * @param evt
	 */
	public void aboutToAddConnection(ConnectionEvent evt);
	
	/**
	 * Called before a connection is removed from an element.
	 * This method is dispatched once for both the source and target
	 * of the association.
	 * @param evt
	 */
	public void aboutToRemoveConnection(ConnectionEvent evt);
	
	/**
	 * Called before a property on an element is changed.
	 * @param evt
	 */
	public void aboutToChangeProperty(PropertyEvent evt);
	
}
```

Now, to veto an event, we can register a PropsedChangeListener:

```
ClientModelObject readOnlyObject = ...;
ProposedChangeListener vetoEverything = new ProposedChangeListener(){

	public void aboutToAddChild(ContainmentEvent evt){evt.veto();}
	
	public void aboutToRemoveChild(ContainmentEvent evt){evt.veto();}
	
	public void aboutToAddConnection(ConnectionEvent evt){evt.veto();}
	
	public void aboutToRemoveConnection(ConnectionEvent evt){evt.veto();}
	
	public void aboutToChangeProperty(PropertyEvent evt){evt.veto();}
};

readOnlyObject.addProposedChangeListener(vetoEverything);
```

# Global Event Listening Across a Model or Multiple Models #

GEMS uses a centralized event dispatcher: org.gems.ajax.client.model.event.EventDispatcher. This singleton class can be used to listen to events from every ClientModelObject -- regardless of the model it is attached to. To receive notification of all events across all ClientModelObjects BEFORE they are dispatched to a ClientModelObject's local listeners, register a ModelListener with the EventDispatcher as follows:

```
ModelListener mygloballistener = ...;
EventDispatcher.get().getPreDispatchListeners().add(l);
```

To receive notification of all events across all ClientModelObjects AFTER they are dispatched to a ClientModelObject's local listeners, register a ModelListener with the EventDispatcher as follows:

```
ModelListener mygloballistener = ...;
EventDispatcher.get().getPostDispatchListeners().add(l);
```

# Stopping All Events in the System #

Sometimes, it can be useful to temporarily suspend all event dispatching in GEMS. To stop any events from being sent to any listeners -- even local listeners -- invoke the disableEvents() method:

```
//Stop all events in the system
EventDispatcher.get().disableEvents();

....//do some stuff

//Restart event dispatching
EventDispatcher.get().enableEvents();
```

Any events fired after disableEvents() is called will be ignored and lost forever.

# Stopping All Events in the System without Losing Events #

If you want to stop dispatching events but record them so they are not lost, you can use the disableAndRecordEvents method. This procedure works as follows:

```
//Stop all events in the system
EventDispatcher.get().disableAndRecordEvents();

....//do some stuff

//Restart event dispatching
EventDispatcher.get().enableEventsAndDisableRecording();
List<ModelEvent> recordedEvents = EventDispatcher.get().getRecordedEvents();

//do something with the recorded events...

//cleanup the event buffer
EventDispatcher.get().releaseRecordedEvents();
```

# Recording Events in GEMS #

All events can be recorded as they pass through the EventDispatcher. To begin recording events, simply call startRecordingEvents():

```
//Start recording events
EventDispatcher.get().startRecordingEvents();

....//do some stuff


EventDispatcher.get().stopRecordingEvents();
List<ModelEvent> recordedEvents = EventDispatcher.get().getRecordedEvents();

//do something with the recorded events...

//cleanup the event buffer
EventDispatcher.get().releaseRecordedEvents();
```

By default, events that are vetoed or sent while event dispatching is diabled are not recorded. To enable recording of vetoed events or while the event dispatcher is disabled, call the setRecordVetoedEvents(boolean) method:

```
//Start recording events
EventDispatcher.get().setRecordVetoedEvents(true);
EventDispatcher.get().startRecordingEvents();
....
```