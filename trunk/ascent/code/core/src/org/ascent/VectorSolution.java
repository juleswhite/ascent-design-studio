package org.ascent;


public class VectorSolution {
	
	private Object artifact_;
	private int[] position_;

	public VectorSolution(int[] position) {
		super();
		position_ = position;
	}

	public int[] getPosition() {
		return position_;
	}

	public void setPosition(int[] position) {
		position_ = position;
	}
	
	public VectorSolution cloneSolution(){
		int[] pos = new int[position_.length];
		System.arraycopy(position_, 0, pos, 0, pos.length);
		return new VectorSolution(pos);
	}

	public Object getArtifact() {
		return artifact_;
	}

	public void setArtifact(Object artifact) {
		artifact_ = artifact;
	}
	
	public void changed(){
		artifact_ = null;
	}
	
	public String toString(){
		String str = "[";
		for(int i = 0; i < position_.length; i++){
			if(i != 0)
				str += ",";
			str += position_[i];
		}
		str += "]";
		return str;
	}
}
