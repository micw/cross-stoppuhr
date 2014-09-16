/*
 * Created by JFormDesigner on Mon Sep 01 20:48:11 CEST 2014
 */

package de.wyraz.cross.stoppuhr.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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
import javax.swing.table.DefaultTableModel;

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
		
		stoppuhr.start();
		
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
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0), "spacebar");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "enter");
		
		getActionMap().put("escape",new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fldStartnummer.setText("");
				fldStartnummer.requestFocus();
			}
		});
		getActionMap().put("enter",new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stoppuhr.addNummer(fldStartnummer.getText());
				fldStartnummer.setText("");
				fldStartnummer.requestFocus();
			}
		});
		getActionMap().put("spacebar",new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stoppuhr.addZeit();
			}
		});

		// http://tips4java.wordpress.com/2008/12/12/table-stop-editing/
		tblZeiten.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		
		tblZeiten.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.CTRL_DOWN_MASK), "delete");
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
		
		
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label1 = new JLabel();
		fldStartzeit = new JLabel();
		label3 = new JLabel();
		fldZeit = new JLabel();
		label2 = new JLabel();
		fldStartnummer = new JTextField();
		scrollPane1 = new JScrollPane();
		tblZeiten = new JTable();
		label4 = new JLabel();

		//======== this ========
		setBackground(Color.white);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new FormLayout(
			"default, $lcgap, default, $ugap, default, $lcgap, default",
			"2*(default, $lgap), default:grow, $lgap, default"));

		//---- label1 ----
		label1.setText("Startzeit:");
		label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 5f));
		add(label1, CC.xy(1, 1));

		//---- fldStartzeit ----
		fldStartzeit.setText("00:00:00");
		fldStartzeit.setFont(fldStartzeit.getFont().deriveFont(fldStartzeit.getFont().getStyle() | Font.BOLD, fldStartzeit.getFont().getSize() + 5f));
		add(fldStartzeit, CC.xy(3, 1));

		//---- label3 ----
		label3.setText("Zeit:");
		label3.setFont(label3.getFont().deriveFont(label3.getFont().getStyle() | Font.BOLD, label3.getFont().getSize() + 5f));
		add(label3, CC.xy(5, 1));

		//---- fldZeit ----
		fldZeit.setText("0:00:00");
		fldZeit.setFont(fldZeit.getFont().deriveFont(fldZeit.getFont().getStyle() | Font.BOLD, fldZeit.getFont().getSize() + 5f));
		add(fldZeit, CC.xy(7, 1));

		//---- label2 ----
		label2.setText("Eingabe Startnummer:");
		add(label2, CC.xy(1, 3));

		//---- fldStartnummer ----
		fldStartnummer.setFont(fldStartnummer.getFont().deriveFont(fldStartnummer.getFont().getStyle() | Font.BOLD, fldStartnummer.getFont().getSize() + 5f));
		add(fldStartnummer, CC.xy(3, 3));

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
		add(scrollPane1, CC.xywh(1, 5, 7, 1, CC.DEFAULT, CC.FILL));

		//---- label4 ----
		label4.setText("Einf\u00fcgen: Strg + E | L\u00f6schen: Strg + L");
		add(label4, CC.xywh(1, 7, 7, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label1;
	private JLabel fldStartzeit;
	private JLabel label3;
	private JLabel fldZeit;
	private JLabel label2;
	private JTextField fldStartnummer;
	private JScrollPane scrollPane1;
	private JTable tblZeiten;
	private JLabel label4;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
