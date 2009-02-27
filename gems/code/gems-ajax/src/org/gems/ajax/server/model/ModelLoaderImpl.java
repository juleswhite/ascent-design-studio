package org.gems.ajax.server.model;

import java.util.ArrayList;
import java.util.List;

import org.gems.ajax.client.model.BasicModelHelper;
import org.gems.ajax.client.model.ClientModelObject;
import org.gems.ajax.client.model.EnumProperty;
import org.gems.ajax.client.model.MetaAssociation;
import org.gems.ajax.client.model.MetaType;
import org.gems.ajax.client.model.ModelingPackage;
import org.gems.ajax.client.model.Property;
import org.gems.ajax.client.model.TypeManager;
import org.gems.ajax.client.model.resources.ModelResource;
import org.gems.ajax.server.model.emf.EMFModelLoader;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ModelLoaderImpl extends RemoteServiceServlet implements ModelLoader{

//	private ClientModelObject root_;
//	private Map<String,ModelReader> readers_ = new HashMap<String, ModelReader>();
	private List<ModelPostprocessor> postProcessors_ = new ArrayList<ModelPostprocessor>();
	
	public ModelLoaderImpl(){
//		root_ = createModel();
		postProcessors_.add(new GemsAssociationPostProcessor());
	}
	
	public ClientModelObject createModel(){
		MetaType mt = TypeManager.getOrCreateTypeForName("default","ClientModelObject");
		mt.getValidChildTypes().add(mt);
		MetaAssociation ma = new MetaAssociation("Conn",mt,mt);
		mt.getAssociations().add(ma);
		
		ClientModelObject root = new ClientModelObject("root",mt);
		root.addChild(new ClientModelObject("f1",mt));
		root.addChild(new ClientModelObject("f2",mt));
		root.addChild(new ClientModelObject("f3",mt));
		
		
		
		ClientModelObject f4 = new ClientModelObject("f4",mt);
		

		f4.getProperties().put("boolean",new Property("boolean",Property.BOOLEAN,false));
		f4.getProperties().put("int",new Property("int",Property.INT,2));
		f4.getProperties().put("string",new Property("string",Property.STRING,"my string"));
		f4.getProperties().put("decimal",new Property("decimal",Property.DECIMAL,"1.0"));
		ArrayList<String> pvals = new ArrayList<String>();
		pvals.add("a");
		pvals.add("b");
		pvals.add("c");
		f4.getProperties().put("enum",new EnumProperty("enum","a",pvals));
		root.addChild(f4);
		
		return root;
	}
	
	public ClientModelObject loadModel(String id) {
		ModelReader reader = getReader(id);
		Model model = reader.loadModel(id);
		
		for(ModelPostprocessor proc : postProcessors_)
			model = proc.process(model);
		
		return createModel();//model.getRoot();
	}
	
	public ModelingPackage loadModel(ModelResource res){
		ModelingPackage pkg = new ModelingPackage();
		pkg.setRootObject(loadModel(""));
		pkg.setModelHelper(new BasicModelHelper());
		pkg.setModelResource(res);
		pkg.setModelType(TypeManager.getModelTypeForName("default"));
		return pkg;
	}

	public ModelReader getReader(String id){
		return new EMFModelLoader();
	}
}
