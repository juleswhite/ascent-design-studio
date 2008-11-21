package org.ascent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BasicBinPacker {
	List<ClassicItem> itemsToPack_;
	/*
	 * Item has :
	 * consumedResources
	 */
	List<ClassicBin> bins_;
	/*
	 * bins has: 
	 * List of items in bin
	 * Space for each consumedResource (see Lei Layland
	 */
	
	
	public BasicBinPacker (List<ClassicItem> items, List<ClassicBin> bins){
		itemsToPack_ = items; //Need to sort the items here
		bins_ = bins;
		Comparator <ClassicBin> binComp = new Comparator<ClassicBin>(){
		
			
			public int compare(ClassicBin arg0, ClassicBin arg1) {//decreasing sort bins
				// TODO Auto-generated method stub
				if(arg0.getSpaceLeft_()[0] == arg1.getSpaceLeft_()[0])
				return 0;
				
				else if(arg0.getSpaceLeft_()[0] > arg1.getSpaceLeft_()[0])
					return -1;
				
				return 1;
			}
			
			
			
		};
		
		Comparator <ClassicItem> itemComp = new Comparator<ClassicItem>(){
		
			
			public int compare(ClassicItem arg0, ClassicItem arg1) {//decreasing sort bins
				// TODO Auto-generated method stub
				if(arg0.getConsumedResources_()[0] == arg1.getConsumedResources_()[0])
					return 0;
				
				if(arg0.getConsumedResources_()[0] > arg1.getConsumedResources_()[0])
					return -1;
				
				return 1;
			}
			
			
			
		};
		Collections.sort(bins_,binComp);
		Collections.sort(itemsToPack_,itemComp);
		//Collections.sort(bins,  )
	}
	
	
	public boolean packItems(){
		boolean success;
		for( ClassicItem item : itemsToPack_){
			success =placeItem(item);
			if(success == false){
				System.out.println("Unsuccessful. Failed on " + item.getName());
				
				return false;
			}
		}
		return true;
	}
	
	private boolean placeItem(ClassicItem item){
		for(ClassicBin bin : bins_){
			
			if( fitsBin(item, bin)){
				// put it in the bin
				
			
			//System.out.println(" " + bin.getBinName_() +" can hold item " + item.getName());
			bin = putInBin(item, bin); // this will change in the original bin right?
			
		//	bins_.remove(bin.getBinId_());
		//	bins_.add(  bin.getBinId_(), bin);
			return true;
			}
			
		}
		
		return false;
	}
	
	private boolean fitsBin(ClassicItem item, ClassicBin bin){
		double [] consumedResources  = item.getConsumedResources_();
		double [] spaceLeft = bin.getSpaceLeft_();
	//	System.out.println("Spaceleft is " + spaceLeft.length);
		if(sumItems(bin,item) > getLLBound(bin,item)){
		//	System.out.println(item.getName()+ " can't fit " + bin.getBinName_());
			return false;
		}
		
		for(int i = 1; i <spaceLeft.length; i++){
		//	System.out.println("CR [i] = "+ consumedResources[i] +" & space Left = " +spaceLeft[i] );
			if(consumedResources[i] > spaceLeft[i]){
				
				return false; // some resource of the item doens't fit that dimension of the bin.
			}
			
		}
		return true; // all resources fit
		
	}
	
	private ClassicBin putInBin(ClassicItem item, ClassicBin bin){
		//System.out.println("About to add an item to Bin");
		bin.addItem(item);
		double [] sl = bin.getSpaceLeft_();
		double []cr = item.getConsumedResources_();
		for( int i = 1; i < cr.length; i++){
			sl[i] = sl[i] - cr[i];
		}
		bin.setSpaceLeft_(sl);
		return bin;
	}
	
	private double sumItems(ClassicBin bin, ClassicItem item){
		List<ClassicItem>Bitems = bin.getItems();
		double sum  = 0;
		for(ClassicItem bitem : Bitems){
			sum += bitem.getConsumedResources_()[0];
		}
		//System.out.println(" bin has " + bin.getItems().size() + " items");
		return sum+=item.getConsumedResources_()[0];
	}
	
	private double getLLBound(ClassicBin LLbin, ClassicItem item){
		List<ClassicItem> hypotheticalItems = LLbin.getItems();
		//double [] cr = {100.0,100.0,100.0};
		//Item decoy = new Item(100,"100",cr);
		hypotheticalItems.add(item);
		double LLbound = hypotheticalItems.size() * ( Math.pow(2, (1.0 / hypotheticalItems.size()))-1 );
		System.out.print(" LL bound for bin " + LLbin.getBinId_() + " is " + LLbound);
		hypotheticalItems.remove(item);//Weird.
		return LLbound;
	}
	
	public List<ClassicBin> getBins(){
		return bins_;
	}
	
	
	public static void main(String args[]){
		double [] item1Cr = {.05,6.0,3.0};
		double [] item2Cr = {.20,8.0,3.0};
		double [] item3Cr = {.35,4.0,3.0};
		double [] item4Cr = {.15,2.0,3.0};
		double [] item5Cr = {.75,5.0,3.0};
		ClassicItem item1 = new ClassicItem(1,"Item 1", item1Cr);
		ClassicItem item2 = new ClassicItem(2,"Item 2", item2Cr);
		ClassicItem item3 = new ClassicItem(3,"Item 3", item3Cr);
		ClassicItem item4 = new ClassicItem(4,"Item 4", item4Cr);
		ClassicItem item5 = new ClassicItem(5,"Item 5", item5Cr);
		List <ClassicItem> items = new ArrayList();
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		
		double [] bin1Cr = {.694,10.0,7.0};
		double [] bin2Cr = {.694,114.0,10.0};
		double [] bin3Cr = {.694,10.0,5};
		double [] bin4Cr = {.694,7.0,10.0};
		double [] bin5Cr = {.694,9.0,6.0};
		
		ClassicBin bin1 = new ClassicBin("bin 1 ", 1, bin1Cr);
		ClassicBin bin2 = new ClassicBin("bin 2 ", 2, bin2Cr);
		ClassicBin bin3 = new ClassicBin("bin 3 ", 3, bin3Cr);
		ClassicBin bin4 = new ClassicBin("bin 4 ", 4, bin4Cr);
		ClassicBin bin5 = new ClassicBin("bin 5 ", 5, bin5Cr);
		
		List <ClassicBin> bins = new ArrayList();
		bins.add(bin1);
		bins.add(bin2);
		bins.add(bin3);
		bins.add(bin4);
		bins.add(bin5);
		BasicBinPacker bfp = new BasicBinPacker(items,bins);
		List<ClassicBin> doneBins = bfp.getBins();
		System.out.println("doneBins size is " + doneBins.size());
		for(ClassicBin b : doneBins){
			
			System.out.println("Bin " + b.getBinName_() + "has items "+ b.getItems().size());
		}
		bfp.packItems();
		doneBins = bfp.getBins();
		System.out.println(" done Bins size is " + doneBins.size());
		for(ClassicBin b : doneBins){
			
			System.out.println("Bin " + b.getBinName_() + "has items "+ b.getItems().size());
		}
		
		
	
	}
     

}
