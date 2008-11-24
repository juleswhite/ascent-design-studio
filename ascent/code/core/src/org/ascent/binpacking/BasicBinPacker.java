/**************************************************************************
 * Copyright 2008 Brian Dougherty                                          *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.ascent;

import java.awt.Component;
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
		List <ClassicBin> bins = new ArrayList();
		List <ClassicItem> items = new ArrayList(); 
		Node p1 = problem.addNode("P1", new int[] { 100 });
		ClassicBin b1 = new ClassicItem(1,"P1",new int [] {100});
		bins.add(b1);
        Node p2 = problem.addNode("P2", new int[] { 100 });
        ClassicBin b2 = new ClassicItem(2,"P2",new int [] {100});
        bins.add(b2);
        Node p21 = problem.addNode("P21", new int[] { 100 });
        ClassicBin b3 = new ClassicItem(3,"P21",new int [] {100});
        bins.add(b3);
        Node p22 = problem.addNode("P22", new int[] { 100 });
        ClassicBin b4 = new ClassicItem(1,"P22",new int [] {100});
        bins.add(b4);
        Node p23 = problem.addNode("P23", new int[] { 100 });
        ClassicBin b5 = new ClassicItem(1,"P23",new int [] {100});
        bins.add(b5);
        Node p24 = problem.addNode("P24", new int[] { 100 });
        ClassicBin b6 = new ClassicItem(1,"P24",new int [] {100});
        bins.add(b6);
        Node p25 = problem.addNode("P25", new int[] { 100 });
        ClassicBin b7 = new ClassicItem(1,"P25",new int [] {100});
        bins.add(b7);
        Node p26 = problem.addNode("P26", new int[] { 100 });
        ClassicBin b8 = new ClassicItem(1,"P26",new int [] {100});
        bins.add(b8);
        Node p27 = problem.addNode("P27", new int[] { 100 });
        ClassicBin b9 = new ClassicItem(1,"P27",new int [] {100});
        bins.add(b9);
        Node p28 = problem.addNode("P28", new int[] { 100 });
        ClassicBin b10 = new ClassicItem(1,"P28",new int [] {100});
        bins.add(b10);
        Node p29 = problem.addNode("P29", new int[] { 100 });
        ClassicBin b11 = new ClassicItem(1,"P29",new int [] {100});
        bins.add(b11);
        Node p30 = problem.addNode("P30", new int[] { 100 });
        ClassicBin b12 = new ClassicItem(1,"P30",new int [] {100});
        bins.add(b12);
        Node p31 = problem.addNode("P31", new int[] { 100 });
        ClassicBin b13 = new ClassicItem(1,"P31",new int [] {100});
        bins.add(b13);
        Node p32 = problem.addNode("P32", new int[] { 100 });
        ClassicBin b14 = new ClassicItem(1,"P32",new int [] {100});
        bins.add(b14);
        
        Component a1 = problem.addComponent("App1", new int[] { 31 });
        ClassicItem i1 = new ClassicItem(1,"App1",new int [] {31});
        items.add(i1);
        Component a2 = problem.addComponent("App2", new int[] { 31 });
        ClassicItem i2 = new ClassicItem(2,"App2",new int [] {31});
        items.add(i2);
        Component a3 = problem.addComponent("App3", new int[] { 94 });
        ClassicItem i3 = new ClassicItem(3,"App3",new int [] {94});
        items.add(i3);
        Component a4 = problem.addComponent("App4", new int[] { 35 });
        ClassicItem i4 = new ClassicItem(4,"App4",new int [] {35});
        items.add(i4);
        Component a5 = problem.addComponent("App5", new int[] { 44 });
        ClassicItem i5 = new ClassicItem(5,"App5",new int [] {44});
        items.add(i5);
        Component a6 = problem.addComponent("App6", new int[] { 43 });
        ClassicItem i6 = new ClassicItem(6,"App6",new int [] {43});
        items.add(i6);
        Component a7 = problem.addComponent("App7", new int[] { 30 });
        ClassicItem i7 = new ClassicItem(7,"App7",new int [] {30});
        items.add(i7);
        Component a8 = problem.addComponent("App8", new int[] { 58 });
        ClassicItem i8 = new ClassicItem(8,"App8",new int [] {58});
        items.add(i8);
        Component a9 = problem.addComponent("App9", new int[] { 98 });
        ClassicItem i9 = new ClassicItem(9,"App9",new int [] {98});
        items.add(i9);
        Component a10 = problem.addComponent("App10", new int[] { 27 });
        ClassicItem i10 = new ClassicItem(10,"App10",new int [] {27});
        items.add(i10);
        Component a11 = problem.addComponent("App11", new int[] { 54 });
        ClassicItem i11 = new ClassicItem(11,"App11",new int [] {54});
        items.add(i11);
        Component a12 = problem.addComponent("App12", new int[] { 96 });
        ClassicItem i12 = new ClassicItem(12,"App12",new int [] {96});
        items.add(i12);
        Component a13 = problem.addComponent("App13", new int[] { 41 });
        ClassicItem i13 = new ClassicItem(13,"App13",new int [] {41});
        items.add(i13);
        Component a14 = problem.addComponent("App14", new int[] { 37 });
        ClassicItem i14 = new ClassicItem(14,"App14",new int [] {37});
        items.add(i14);
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
		/*
		items.add(item1);
		items.add(item2);
		items.add(item3);
		items.add(item4);
		items.add(item5);
		*/
		double [] bin1Cr = {.694,10.0,7.0};
		double [] bin2Cr = {.694,114.0,10.0};
		double [] bin3Cr = {.694,10.0,5};
		double [] bin4Cr = {.694,7.0,10.0};
		double [] bin5Cr = {.694,9.0,6.0};
		/*
		ClassicBin bin1 = new ClassicBin("bin 1 ", 1, bin1Cr);
		ClassicBin bin2 = new ClassicBin("bin 2 ", 2, bin2Cr);
		ClassicBin bin3 = new ClassicBin("bin 3 ", 3, bin3Cr);
		ClassicBin bin4 = new ClassicBin("bin 4 ", 4, bin4Cr);
		ClassicBin bin5 = new ClassicBin("bin 5 ", 5, bin5Cr);
		*/
		
		/*bins.add(bin1);
		bins.add(bin2);
		bins.add(bin3);
		bins.add(bin4);
		bins.add(bin5);*/
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
