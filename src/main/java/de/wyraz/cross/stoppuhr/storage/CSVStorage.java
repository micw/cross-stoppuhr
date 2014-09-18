package de.wyraz.cross.stoppuhr.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.supercsv.cellprocessor.FmtNumber;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import de.wyraz.cross.stoppuhr.model.Stoppuhr;

public class CSVStorage implements IStorage
{
	protected int getLatest()
	{
		Pattern pattern=Pattern.compile("Stoppuhr_(\\d+).csv");
		File[] files=new File(".").listFiles();
		int max=0;
		if (files!=null) for (File file: files)
		{
			if (!file.isFile()) continue;
			Matcher m=pattern.matcher(file.getName());
			if (m.matches())
			{
				max=Math.max(max, Integer.parseInt(m.group(1)));
			}
		}
		return max;
	}
	
	protected File getFile(int num)
	{
		return new File("Stoppuhr_"+num+".csv");
	}

	
	protected final String[] headers=new String[] {"Zeit","Startnummer"};
	protected final CellProcessor[] cps=new CellProcessor[] { new Optional(), new Optional() };
	
	@Override
	public boolean load(Stoppuhr stoppuhr, int num)
	{
		if (num<=0) return false;
		File file=getFile(num);
		if (file.isFile()) try (Reader reader=new InputStreamReader(new FileInputStream(file),"cp1252"))
		{
			try (CsvMapReader csv=new CsvMapReader(reader,CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE))
			{
                csv.getHeader(true);
                
    			Map<String,Object> values=csv.read(headers, cps);
    			
    			long startzeit=Long.parseLong((String)values.get("Zeit"));

                csv.read(headers, cps); // 1 Zeile überspringen (Startzeit Lokalzeit)
    			
    			List<Integer> zeiten=new ArrayList<>();
    			List<String> nummern=new ArrayList<>();
    			
    			while ((values=csv.read(headers, cps))!=null)
    			{
    				String zeit=(String)values.get("Zeit");
    				zeiten.add(zeit==null?null:new Integer(zeit));
    				nummern.add((String)values.get("Startnummer"));
    			}
    			trimList(zeiten);
    			trimList(nummern);
    			
    			stoppuhr.load(startzeit,zeiten,nummern);
    			return true;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	protected void trimList(List<?> list)
	{
		while (!list.isEmpty()&&list.get(list.size()-1)==null)
		{
			list.remove(list.size()-1);
		}
	}
	
	@Override
	public int loadLatest(Stoppuhr stoppuhr)
	{
		int latest=getLatest();
		if (load(stoppuhr,latest)) return latest;
		return 0;
	}
	
	
	@Override
	public int saveNext(Stoppuhr stoppuhr)
	{
		if (stoppuhr.getStartzeit()<=0) return 0;
		
		int next=getLatest()+1;
		File dest=getFile(next);
		
		try (Writer writer=new OutputStreamWriter(new FileOutputStream(dest),"cp1252"))
		{
			CsvMapWriter csv=new CsvMapWriter(writer,CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
			csv.writeHeader(headers);
			Map<String,Object> values=new HashMap<>();
			values.put("Zeit", stoppuhr.getStartzeit());
			values.put("Startnummer", "Startzeit");
			csv.write(values,headers,cps);
			
			/**
			 * Startzeit in lokaler Zeitzone (für Excel-Import)
			 */
			
			TimeZone tz=TimeZone.getDefault();
            values.put("Zeit", stoppuhr.getStartzeit()+tz.getOffset(stoppuhr.getStartzeit()));
            values.put("Startnummer", "Startzeit Lokalzeit");
			csv.write(values,headers,cps);
			
			for (int i=0;i<stoppuhr.getAnzahlWerte();i++)
			{
				values.put("Zeit", stoppuhr.getZeitAt(i));
				values.put("Startnummer", stoppuhr.getStartnummerAt(i));
				csv.write(values,headers,cps);
			}
			
			csv.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			dest.delete();
		}
		
		
		return 0;
	}
}