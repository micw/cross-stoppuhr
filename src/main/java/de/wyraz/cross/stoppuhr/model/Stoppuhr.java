package de.wyraz.cross.stoppuhr.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;

import de.wyraz.cross.stoppuhr.storage.CSVStorage;
import de.wyraz.cross.stoppuhr.storage.IStorage;


public class Stoppuhr
{
	protected long startzeit;
	protected boolean dirty;
	protected Thread autoSaveThread;
	
	public Stoppuhr()
	{
		saveState=storage.loadLatest(this);
	}
	
	public synchronized void start(boolean force)
	{
		if (startzeit>0 && !force) return;
		startzeit=System.currentTimeMillis();
		zeiten=new ArrayList<>();
		nummern=new ArrayList<>();
		save();
		autoSaveThread=new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning())
				{
					try
					{
						Thread.sleep(15000);
					}
					catch (InterruptedException ex)
					{
						// ignored
					}
					autosave();
				}
			}
		});
		autoSaveThread.start();
	}
	
	
	public boolean isRunning()
	{
	    return startzeit>0;
	}
	
	public int getZeit()
	{
		if (!isRunning()) return 0;
		return (int) (System.currentTimeMillis()-startzeit);
	}
	public long getStartzeit()
	{
		return startzeit;
	}

	public String getZeitFormatted()
	{
		return getZeitFormatted(getZeit(),false);
	}
	
	public String getZeitFormatted(Integer zeit, boolean withMs)
	{
		if (zeit==null) return null;
		int ms=(int) ((zeit%1000)/100);
		int s=zeit/1000;
		int m=s/60;
		int h=m/60;
		
		if (withMs) return String.format("%02d:%02d:%02d.%d", h,m%60,s%60,ms);
		else return String.format("%02d:%02d:%02d", h,m%60,s%60);
	}
	
	public String getStartzeitFormatted()
	{
		if (startzeit<=0) return "--:--:--";
		return new SimpleDateFormat("HH:mm:ss").format(startzeit);
	}
	
	
	protected IStorage storage=new CSVStorage();
	
	protected int saveState=0;
	
	public int getSaveState()
	{
		return saveState;
	}
	
	public synchronized void autosave()
	{
		if (dirty) save();
	}
	
	public synchronized void save()
	{
		saveState=storage.saveNext(this);
		dirty=false;
	}
	
	public synchronized void load(long startzeit, List<Integer> zeiten, List<String> nummern)
	{
		if (startzeit>0)
		{
			this.startzeit=startzeit;
			this.zeiten=new ArrayList<>(zeiten);
			this.nummern=new ArrayList<>(nummern);
			dirty=false;
			fireUpdated();
		}
	}

	protected List<Integer> zeiten;
	protected List<String> nummern;
	
	public int getAnzahlWerte()
	{
		return Math.max(getAnzahlZeiten(),getAnzahlStartnummern());
	}
	public int getAnzahlZeiten()
	{
		if (zeiten==null) return 0;
		return zeiten.size();
	}
	public int getAnzahlStartnummern()
	{
		if (nummern==null) return 0;
		return nummern.size();
	}
	
	public String getStartnummerAt(int pos)
	{
		if (nummern==null) return null;
		if (pos>=nummern.size()) return null;
		
		return nummern.get(pos);
	}
	public String getStartnummerFormattedAt(int pos)
	{
		if (nummern==null) return null;
		if (pos>nummern.size()) return null;
		if (pos==nummern.size()) return ">>><<<";
		
		return nummern.get(pos);
	}
	public Integer getZeitAt(int pos)
	{
		if (zeiten==null) return null;
		if (pos>=zeiten.size()) return null;
		
		return zeiten.get(pos);
	}
	public String getZeitFormattedAt(int pos)
	{
		if (zeiten==null) return null;
		if (pos>zeiten.size()) return null;
		if (pos==zeiten.size()) return ">>><<<";
		
		return getZeitFormatted(zeiten.get(pos),true);
	}
	public synchronized void setZeitFormattedAt(int pos, String value)
	{
		if (zeiten==null) return;
		if (pos>=zeiten.size()) return;
		
		try
		{
			Integer zeit;
			value=value.replaceAll("[^0-9,.]", "").replace(",", ".");
			if (value.length()==0)
			{
				zeit=null;
			}
			else
			{
				Double val=Double.parseDouble(value);
				
				int stunden=(int) (val/10000);
				int minuten=(int) ((val%10000)/100);
				int sekunden=(int) (val%100);
				int millisekunden=(int) ((val*1000)%1000);
				
				zeit=millisekunden+sekunden*1000+minuten*60000+stunden*3600000;
			}
			zeiten.set(pos, zeit);
			dirty=true;
			fireUpdated();
		}
		catch (Exception ex)
		{
			// Ignored
		}
	}
	public synchronized void setStartnummerFormattedAt(int pos, String value)
	{
		if (nummern==null) return;
		if (pos>=nummern.size()) return;
		
		try
		{
			String nummer;
			value=value.replaceAll("[^0-9,.]", "");
			if (value.length()==0)
			{
				nummer=null;
			}
			else
			{
				nummer=value;
			}
			nummern.set(pos, nummer);
			dirty=true;
			fireUpdated();
		}
		catch (Exception ex)
		{
			// Ignored
		}
	}
	
	public boolean isStartnummerDoppeltAt(int pos)
	{
		if (nummern==null) return false;
		if (pos>=nummern.size()) return false;
		
		String nummer=nummern.get(pos);
		if (nummer==null || nummer.trim().length()==0) return false;
		
		for (int i=0;i<nummern.size();i++)
		{
			if (i==pos) continue;
			String andereNummer=nummern.get(i);
			if (andereNummer!=null && nummer.equalsIgnoreCase(andereNummer.trim())) return true;
		}
		return false;
	}
	
	public synchronized void addZeit()
	{
		if (startzeit==0) return;
		zeiten.add(getZeit());
		dirty=true;
		fireUpdated();
	}
	public synchronized void addNummer(String nummer)
	{
		if (startzeit==0) return;
		nummern.add(nummer);
		dirty=true;
		fireUpdated();
	}
	public synchronized void insertZeitAt(int pos)
	{
		if (startzeit==0) return;
		if (pos>=zeiten.size()) zeiten.add(null);
		else zeiten.add(pos,null);
		dirty=true;
		fireUpdated();
	}
	public synchronized void insertNummerAt(int pos)
	{
		if (startzeit==0) return;
		if (pos>=nummern.size()) nummern.add(null);
		else nummern.add(pos,null);
		dirty=true;
		fireUpdated();
	}
	public synchronized void deleteZeitAt(int pos)
	{
		if (startzeit==0) return;
		if (pos>=zeiten.size()) return;
		zeiten.remove(pos);
		dirty=true;
		fireUpdated();
	}
	public synchronized void deleteNummerAt(int pos)
	{
		if (startzeit==0) return;
		if (pos>=nummern.size()) return;
		nummern.remove(pos);
		dirty=true;
		fireUpdated();
	}
	
	protected EventListenerList eventListeners=new EventListenerList();
	
	protected void fireUpdated()
	{
		for (TableModelListener  listener:eventListeners.getListeners(TableModelListener.class))
		{
			listener.tableChanged(null);
		}
	}
	
	public void addTableModelListener(TableModelListener listener)
	{
		eventListeners.add(TableModelListener.class, listener);
	}
	
}
