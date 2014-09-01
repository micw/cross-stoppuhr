package de.wyraz.cross.stoppuhr.ui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import de.wyraz.cross.stoppuhr.model.Stoppuhr;

public class StoppuhrTableModel extends AbstractTableModel
{
	protected final Stoppuhr stoppuhr;
	
	public StoppuhrTableModel(Stoppuhr stoppuhr)
	{
		this.stoppuhr=stoppuhr;
		stoppuhr.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				fireTableDataChanged();
			}
		});
	}
	
	@Override
	public int getColumnCount()
	{
		return 3;
	}
	
	@Override
	public int getRowCount()
	{
		return stoppuhr.getAnzahlWerte()+1;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (columnIndex==0) return String.valueOf(rowIndex+1);

		if (columnIndex==1) return stoppuhr.getZeitFormattedAt(rowIndex);
		
		if (columnIndex==2) return stoppuhr.getStartnummerFormattedAt(rowIndex);
		
		return null;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return columnIndex>0;
	}

}
