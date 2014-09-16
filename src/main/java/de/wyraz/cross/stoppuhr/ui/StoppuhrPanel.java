/*
 * Created by JFormDesigner on Mon Sep 01 20:48:11 CEST 2014
 */

package de.wyraz.cross.stoppuhr.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import de.wyraz.cross.stoppuhr.model.Stoppuhr;
import de.wyraz.cross.stoppuhr.ui.tools.JTextfieldFilter;

/**
 * @author Michael Wyraz
 */
public class StoppuhrPanel extends JPanel {
	
	public static void main(String[] args) {
		JFrame win=new JFrame("Stoppuhr");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Stoppuhr stoppuhr=new Stoppuhr();
		
		win.add(new StoppuhrPanel(stoppuhr));
		win.pack();
		win.setVisible(true);
	}
	
	public StoppuhrPanel()
	{
		this(new Stoppuhr());
	}
	
	protected final Stoppuhr stoppuhr;
	public StoppuhrPanel(final Stoppuhr stoppuhr)
	{
		initComponents();
		
		this.stoppuhr=stoppuhr;
		
		tblZeiten.setModel(new StoppuhrTableModel(stoppuhr));
		
		stoppuhr.start(false);
		
		fldStartzeit.setText(stoppuhr.getStartzeitFormatted());
		
		new Thread() {
			public void run() {
				for (;;)
				{
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							fldZeit.setText(stoppuhr.getZeitFormatted());
						}
					});
					try
					{
						Thread.sleep(100);
					}
					catch (Exception ex)
					{
						
					}
				}
			}
		}.start();
		
		// Space in Eingabefeld deaktivieren
		fldStartnummer.setDocument(new JTextfieldFilter(JTextfieldFilter.NUMERIC));
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "escape");
		getActionMap().put("escape",new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fldStartnummer.setText("");
				fldStartnummer.requestFocus();
			}
		});
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "enter");
		getActionMap().put("enter",new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stoppuhr.addNummer(fldStartnummer.getText());
				fldStartnummer.setText("");
				fldStartnummer.requestFocus();
			}
		});
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0), "spacebar");
		getActionMap().put("spacebar",new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stoppuhr.addZeit();
			}
		});

		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK), "save");
		getActionMap().put("save",new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stoppuhr.save();
				refreshSaveState();
			}
		});
		
		// http://tips4java.wordpress.com/2008/12/12/table-stop-editing/
		tblZeiten.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
		tblZeiten.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_DOWN_MASK), "delete");
		tblZeiten.getActionMap().put("delete", new AbstractAction() {
		    public void actionPerformed(ActionEvent evt) {
		       int row = tblZeiten.getSelectedRow();
		       int col = tblZeiten.getSelectedColumn();
		       if (row >= 0)
		       {
		    	   if (col==1) StoppuhrPanel.this.stoppuhr.deleteZeitAt(row);
		    	   else if (col==2) StoppuhrPanel.this.stoppuhr.deleteNummerAt(row);
		       }
		    }
		});
		
		
		tblZeiten.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.CTRL_DOWN_MASK), "insert");
		tblZeiten.getActionMap().put("insert", new AbstractAction() {
		    public void actionPerformed(ActionEvent evt) {
		       int row = tblZeiten.getSelectedRow();
		       int col = tblZeiten.getSelectedColumn();
		       if (row >= 0)
		       {
		    	   if (col==1) StoppuhrPanel.this.stoppuhr.insertZeitAt(row);
		    	   else if (col==2) StoppuhrPanel.this.stoppuhr.insertNummerAt(row);
		       }
		    }
		});
		
		//http://stackoverflow.com/questions/7197366/jtable-row-selection-after-tablemodel-update
		final AtomicInteger selectedRow=new AtomicInteger(-1);
		final AtomicInteger selectedCol=new AtomicInteger(-1);
		tblZeiten.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				selectedRow.set(tblZeiten.getSelectedRow());
		       	selectedCol.set(tblZeiten.getSelectedColumn());
			}
		});
		tblZeiten.getModel().addTableModelListener(new TableModelListener() {      
		    @Override
		    public void tableChanged(TableModelEvent e)
		    {
		    	TableCellEditor editor=tblZeiten.getCellEditor();
		    	if (editor!=null) editor.cancelCellEditing();
		    	
		    	final int row=selectedRow.get();
		    	final int col=selectedCol.get();
            	if (row<0||col<0) return;
		    	
		        SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
	            		// http://book.javanb.com/the-java-developers-almanac-1-4/egs/javax.swing.table/Sel.html
	            		tblZeiten.changeSelection(row,col, false, false);
		             }
		        });
		    }
		});
		
		tblZeiten.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				if (stoppuhr.isStartnummerDoppeltAt(row)) setBackground(Color.ORANGE);
				else if (isSelected) setBackground(table.getSelectionBackground());
				else setBackground(table.getBackground());
				
				return this;
			}
		});
		
	}
	
	protected void refreshSaveState()
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			fldSaveState.setText(stoppuhr.getSaveState()<=0?"---":""+stoppuhr.getSaveState());
		}
		else
		{
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					refreshSaveState();
				}
			});
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label1 = new JLabel();
		fldStartzeit = new JLabel();
		label3 = new JLabel();
		fldZeit = new JLabel();
		label2 = new JLabel();
		fldStartnummer = new JTextField();
		label5 = new JLabel();
		label6 = new JLabel();
		scrollPane1 = new JScrollPane();
		tblZeiten = new JTable();
		label4 = new JLabel();
		label7 = new JLabel();
		label8 = new JLabel();
		fldSaveState = new JLabel();

		//======== this ========
		setBackground(Color.white);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new FormLayout(
			"86dlu, $lcgap, 61dlu, $ugap, 29dlu, $lcgap, 55dlu",
			"4*(default, $lgap), default:grow, 2*($lgap, default)"));

		//---- label1 ----
		label1.setText("Startzeit:");
		label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 5f));
		add(label1, CC.xy(1, 1));

		//---- fldStartzeit ----
		fldStartzeit.setText("--:--:--");
		fldStartzeit.setFont(fldStartzeit.getFont().deriveFont(fldStartzeit.getFont().getStyle() | Font.BOLD, fldStartzeit.getFont().getSize() + 5f));
		add(fldStartzeit, CC.xy(3, 1));

		//---- label3 ----
		label3.setText("Zeit:");
		label3.setFont(label3.getFont().deriveFont(label3.getFont().getStyle() | Font.BOLD, label3.getFont().getSize() + 5f));
		add(label3, CC.xy(5, 1));

		//---- fldZeit ----
		fldZeit.setText("-:--:--.-");
		fldZeit.setFont(fldZeit.getFont().deriveFont(fldZeit.getFont().getStyle() | Font.BOLD, fldZeit.getFont().getSize() + 5f));
		add(fldZeit, CC.xy(7, 1));

		//---- label2 ----
		label2.setText("Eingabe Startnummer:");
		add(label2, CC.xy(1, 3));

		//---- fldStartnummer ----
		fldStartnummer.setFont(fldStartnummer.getFont().deriveFont(fldStartnummer.getFont().getStyle() | Font.BOLD, fldStartnummer.getFont().getSize() + 5f));
		add(fldStartnummer, CC.xy(3, 3));

		//---- label5 ----
		label5.setText("Zeitnahme: Leertaste | Nummer: Eingabe+Enter");
		add(label5, CC.xywh(1, 5, 7, 1));

		//---- label6 ----
		label6.setText("Mit Escape geht es immer zur Nummerneingabe zur\u00fcck");
		add(label6, CC.xywh(1, 7, 7, 1));

		//======== scrollPane1 ========
		{

			//---- tblZeiten ----
			tblZeiten.setFillsViewportHeight(true);
			tblZeiten.setFont(tblZeiten.getFont().deriveFont(tblZeiten.getFont().getSize() + 3f));
			tblZeiten.setModel(new DefaultTableModel(
				new Object[][] {
					{"", null, null},
					{null, null, null},
				},
				new String[] {
					"POS", "Startnr.", "Zeit"
				}
			));
			tblZeiten.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tblZeiten.setRowSelectionAllowed(false);
			scrollPane1.setViewportView(tblZeiten);
		}
		add(scrollPane1, CC.xywh(1, 9, 7, 1, CC.DEFAULT, CC.FILL));

		//---- label4 ----
		label4.setText("Einf\u00fcgen: Strg + E | L\u00f6schen: Strg + X | \u00c4ndern: Doppelklick");
		add(label4, CC.xywh(1, 11, 7, 1));

		//---- label7 ----
		label7.setText("Speichern: Strg + S");
		add(label7, CC.xywh(1, 13, 3, 1));

		//---- label8 ----
		label8.setText("Stand:");
		add(label8, CC.xy(5, 13));

		//---- fldSaveState ----
		fldSaveState.setText("---");
		add(fldSaveState, CC.xy(7, 13));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label1;
	private JLabel fldStartzeit;
	private JLabel label3;
	private JLabel fldZeit;
	private JLabel label2;
	private JTextField fldStartnummer;
	private JLabel label5;
	private JLabel label6;
	private JScrollPane scrollPane1;
	private JTable tblZeiten;
	private JLabel label4;
	private JLabel label7;
	private JLabel label8;
	private JLabel fldSaveState;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
