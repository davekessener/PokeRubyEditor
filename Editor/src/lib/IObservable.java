package lib;

public interface IObservable
{
	public abstract void addObserver(Observer o);
	public abstract void deleteObserver(Observer o);
}
