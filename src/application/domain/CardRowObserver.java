package application.domain;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CardRowObserver extends Remote {
	
	public void modelChanged(CardRow cardRow) throws RemoteException;
}
