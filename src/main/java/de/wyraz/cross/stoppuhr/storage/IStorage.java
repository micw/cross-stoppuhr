package de.wyraz.cross.stoppuhr.storage;

import de.wyraz.cross.stoppuhr.model.Stoppuhr;

public interface IStorage
{
	public int saveNext(Stoppuhr stoppuhr);
	public int loadLatest(Stoppuhr stoppuhr);
	public boolean load(Stoppuhr stoppuhr, int num);
}
