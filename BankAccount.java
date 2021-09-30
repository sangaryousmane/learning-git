// Understanding the reputable bank account testing
public class BankAccount {
private double withdraw;
private double balance;
private double Account;

public BankAccount(double balance){
this.balance=balance;

}
public void setWithdraw(double withdraw){
this.withdraw=withdraw;
}

public void setBalance(double balance){
this.balance=balance;
}

public void setAccount(double Account){
this.Account=Account;
}
public double getWithdraw(){
return withdraw;
}
public double getBalance(){
return balance;
}
public void getAccount(){
this.Account=Account;
}
public double getDifference(){
double difference;

if (this.balance - this.withdraw<0){
System.out.println("Hustle: "+ this.getBalance());
}
if (this.withdraw-this.balance>0){
System.out.println("You are excellently rich");
}
return 1;
}
public static void main(String[] args) {
BankAccount account=new BankAccount(300);
account.setAccount(900);
account.setWithdraw(100);
System.out.println("Balance: " + account.getBalance());
System.out.println("The difference: " + account.getDifference());

 }
}

