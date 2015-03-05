package me.belka.legendcoins;

public class EconomyPlayer {

	private int balance;
	private String name;
	private int id;
	
	public EconomyPlayer(int id, int balance, String name) {
		this.balance = balance;
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getId() {
		return id;
	}
	
}
