# Introduction #

GEMS uses an element's type information to look up a template to use to generate the view for a model element. Templates can be written in multiple languages -- as long as GEMS has the appropriate TemplateExecutor installed. The next sections of this tutorial are going to focus on GEMS' default template language. Detailed instructions for creating a new templating language or template resolution strategy are near the end of this guide.

# How Templates are Resolved #

Each element in a model has a type associated with it. Furthermore, each model also has a type associated with it.

```
MetaType mt = TypeManager.getOrCreateTypeForName(
                                         "default", //the model's type
                                         "ClientModelObject" //the element's type
                                           );
ClientModelObject root = new ClientModelObject("root",mt);
```



By default, GEMS looks through the "templates" subdirectory of its installation path to find a template for an element. For example, to find a template for an element of type "ClientModelObject" attached to a model of type "default", GEMS will search for files matching the pattern:

`<GEMS_INSTALL_DIR>/templates/default/ClientModelObject.extension`

GEMS will match the element against templates with any arbitrary file extension e.g.
```
ClientModelObject.htm
ClientModelObject.foo
etc.
```

The file extension is used to determine the TemplateExecutor implementation that should be used to interpret it. You should add templates for your model elements by creating a new subdirectory of your templates directory corresponding to the name of the model type and then placing appropriately named template files within it.

# Default GEMS Template Language #

The default GEMS template language is attached to the ".htm" file extension. The default template language allows you to bind values from the client and the model to the html. For example, the template:

```
<html>
 <head></head>
 <body>
  <div style="width:${width}; height:${height}">
   <b>My Element ID is ${id}!</b>
  </div>
 </body>
</html>
```

uses the ${width} and ${height} placeholders to insert the client view's current width and height. If the user resizes the element's view, the template is re-interpreted and these values are updated. Similarly, the ${id} property is used to get the id of the model element that the view is bound to. Properties that are installed on the element can be referred to via ${model.propertyname}.

The standard GEMS template language can be run on either the client or the server. To have the template run on the client and not require a roundtrip to the server on updates, the template's very first line should be:
```
<!--GEMS_Client-->
```

# Interpretation of HTML on the Client #

Arbitrary HTML code can be sent to the client as the result of interpreting a template. The code must only follow a few small conventions:

  1. the HTML must have `<html>` tags surrounding it
  1. all javascripts and CSS stylesheets that are loaded must be referenced in a `<head>` tag section that follows the `<html>` tag
  1. the HTML must have a valid `<body>` tag section

For example, valid HTML to return could look like:

```
<html>
<head>
<link rel="stylesheet" type="text/css" href="js/lightbox/lightbox.css" />
<script type="text/javascript" 
        src="js/lightbox/lightbox.js" 
        readyif="window.initLightbox != null" 
        init="initLightbox();"  
        onupdate="initLightbox();">
</script>
</head>

<body>

<div style="width:100%; height:100%">
  <img src="img/block.png">
</div>

<div style="position:absolute; top:0; left:0; margin:10 10 10 10;">
Model Object:${id}
<a href="img/block.jpg" rel="lightbox" title="my caption"><img src="img/b.gif" /></a> 
</div>
</body>
</html>
```

This HTML loads a javascript and a CSS stylesheet in its head. Any javascripts or stylesheets that are returned with the template HTML are dynamically loaded into the client browser. Furthermore, GEMS guarantees that the same script or stylesheet is loaded exactly once regardless of how many times the javascript or stylesheet shows up in HTML returned from templates to the client.

Because the javascript is loaded dynamically, it cannot use the document's onLoad method to execute. GEMS provides hooks that can be used to:
  1. Check to make sure your script is loaded properly
  1. Call a method on your script to initialize it the first time it is loaded (from any template)
  1. Call a method on your script each time the template is re-interpreted and the result is sent back to the browser

The hooks can be seen in the following code snippet:
```
<script type="text/javascript" 
        src="js/lightbox/lightbox.js" 
        readyif="window.initLightbox != null"  
        init="initLightbox();"  
        onupdate="initLightbox();">
</script>
```

The "readyif" attribute specifies an optional javascript expression to execute via "eval()" to determine if your script has finished loading. The javascript expression MUST RETURN A BOOLEAN. The "init" attribute specifies an optional javascript expression to execute via "eval()" once the script has successfully loaded (**'readyif' must be specified if 'init' or 'update' are used**). This expression will be executed once for the entire modeling session. If other templates reference the script and the init function, the script will only be loaded once and the init function called a single time. The "onupdate" attribute specifies an optional javascript expression to run each time the template is re-interpreted and the result has been loaded into the view on the client.

The template language also supports a number of properties that can be set to initialize the size and other settings of the view. These properties should be inserted into a comment beginning with "<!--GEMS\_Props" as follows:
```
<!--GEMS_Server-->

<!--GEMS_Props
TemplateType:Simple;
InitWidth:150px;
InitHeight:100px;
InitX:45px;
InitY:50px;
Resizeable:true;
Moveable:true;
-->

<html>
<head>
...
</head>
</body>
</html>
```

GEMS currently supports the following properties in the GEMS\_Props directive:
  1. InitWidth - specifies the initial width of the view. Can be set to any valid value for the CSS width property.
  1. InitHeight - specifies the initial height of the view. Can be set to any valid value for the CSS width property.

# Creating New Template Languages #

Arbitrary template languages can be created for GEMS. The only requirement is that the execution of the template returns a valid HTML string that meets the specifications from the previous section. There are three key steps to creating a new template language and installing it in GEMS:
  1. Implement a new org.gems.ajax.server.figures.templates.ExecutorFactory
  1. Implement a new org.gems.ajax.server.figures.templates.TemplateExecutor
  1. Install the new ExecutorFactory into GEMS' gems-server.xml

The ExecutorFactory is a class that creates executable versions of each script. The ExectuorFactory interface has a single method:

```
/**
 * An executor factory is used to create executors for a given template
 * language type. The executor factories should be registered with the
 * DefaultServerTemplateManager.
 */
public interface ExecutorFactory {
	
	/**
	 * Create a template executor from a string of template
	 * data.
	 * 
	 * @param template
	 * @return
	 */
	public TemplateExecutor createExecutor(String template);
}
```

The ExecutorFactory is passed the contents of the template file from disk that it is being asked to turn into a TemplateExecutor. The factory must compile and create an instance of a concrete subclass of the TemplateExecutor interface.

The TemplateExecutor is used to execute the template each time the model changes and the client requests for its view to be updated. The TemplateExecutor interface has a single method that takes the new state of the client and model (wrapped as a TemplateExecData object):

```
/**
 * This interface should be implemented to create a new template
 * language and plug it into the server-side template engine.
 * An executor can be registered with the DefaultServerTemplateManager
 * and run whenever a template is requested of the type handled
 * by this template executor.
 */
public interface TemplateExecutor {

	/**
	 * This method should execute the template with
	 * the provided data and produce html that can
	 * be displayed by the view on the client-side.
	 * 
	 * @param data
	 * @return
	 */
	public String exec(TemplateExecData data);
	
}

```

The TemplateExecutor's exec method should execute the template using the new client and model state data and then return a new HTML result. The HTML is passed on to the client and loaded into that model element's view.

Once the ExecutorFactory and TemplateExecutor implementations are created, they must be installed into GEMS. The installation is accomplished by creating a new bean (see the Java Spring framework documentation for more details on this file) to instantiate the ExecutorFactory and then referencing it in the executorFactories map of the serverTemplateManager:

```
<bean name="serverTemplateManager" class="org.gems.ajax.server.figures.templates.DefaultServerTemplateManager">
    ....
    <property name="executorFactories">
      <map>
        <entry>
            <key>
                <!-- the file extension to register your template type with -->
                <value>htm</value>
            </key>
            <!-- the reference to your executor factory -->
            <ref bean="simpleTemplateFactory" />
        </entry>
      </map>
    </property>
  </bean>
  
  <!-- the instantiation of your executor factory -->
  <bean name="simpleTemplateFactory" 
        class="org.gems.ajax.server.figures.templates.SimpleExecutorFactory"/>
```

# Overriding the Default Template Resolution Mechanism #

The default template resolution procedure of looking through the templates directory can be changed by plugging in a new implementation of TemplateFinder. To change the resolution mechanism, implement a new implementation of org.gems.ajax.server.figures.templates.TemplateFinder

The TemplateFinder interface has a single method which is invoked to get a handle on an input stream for template for the requested object type. The data is returned as an instance of the ResolvedTemplate class. The ResolvedTemplate instance MUST have a value set for the type. The TemplateFinder is passed the model type and model element type of the element that a template is being requested for.

To plug-in a new TemplateFinder, modify gems-server.xml as follows:

```
<bean name="serverTemplateManager" class="org.gems.ajax.server.figures.templates.DefaultServerTemplateManager">
    <!-- A reference to your implementation of TemplateFinder -->
    <property name="templateFinder" ref="myTemplateFinder"/>
   ....
</bean>

<!-- Instantiate your template finder -->
<bean id="myTemplateFinder" class="org.your.pkg.MyTemplateFinder"/>         
```

Please see: [org.gems.ajax.server.figures.templates.DefaultTemplateFinder](http://code.google.com/p/ascent-design-studio/source/browse/trunk/gems/code/gems-ajax/src/org/gems/ajax/server/figures/templates/DefaultTemplateFinder.java) for an example implementation.