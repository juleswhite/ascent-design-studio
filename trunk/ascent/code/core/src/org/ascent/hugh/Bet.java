package org.ascent.hugh;

import org.ascent.hugh.State;

public class Bet  {
	private double betAmount_;
	private State betState_;
	
	public Bet(){
		
	}
	
	public Bet(State betState){
		betState_ = betState;
	}
	public Bet(State betState, double betAmount){
		betState_ = betState;
		betAmount_ = betAmount;
	}

	public double getBetAmount_() {
		return betAmount_;
	}

	public void setBetAmount_(double betAmount_) {
		this.betAmount_ = betAmount_;
	}

	public State getBetState_() {
		return betState_;
	}

	public void setBetState_(State betState_) {
		this.betState_ = betState_;
	}
	
	
	
}
