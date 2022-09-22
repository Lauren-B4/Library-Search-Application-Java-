import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.sql.*;

import java.util.ArrayList;

import env;


public class Desktop extends JFrame implements ActionListener{
	
	private JLabel tit, aut, year, isbn;
	private JTextField ti, au, ye, is;
	JButton b;
	Connection conn= null;
	public Desktop()
	{ 
		super("Desktop");
		
		//create content pane
		Container c = getContentPane(); 
		c.setLayout(new FlowLayout(FlowLayout.CENTER));
		try {
			Env.databaseConn;
		}
		catch(ClassNotFoundException x) {
			System.out.println("Driver Exceptions");
		}
		catch(SQLException y) {
			System.out.println("SQL Exceptions");
		}
		
		//create labels
		tit = new JLabel("Title:  ", JLabel.LEFT);
		aut =  new JLabel("Author: ",JLabel.LEFT);
		year = new JLabel("Year:   ", JLabel.LEFT);
		isbn = new JLabel("ISBN:   ", JLabel.LEFT);
		
		//create text fields
		ti = new JTextField(30);
		ti.setHorizontalAlignment(JTextField.LEFT);
		au = new JTextField(30);
		au.setHorizontalAlignment(JTextField.LEFT);
		ye = new JTextField(30);
		ye.setHorizontalAlignment(JTextField.LEFT);
		is= new JTextField(30);
		is.setHorizontalAlignment(JTextField.LEFT);
		
		//create button
		b = new JButton("search");
		b.setAlignmentY(BOTTOM_ALIGNMENT);
		
		//add labels and text fields to container
		c.add(tit);
		c.add(ti);
		c.add(aut);
		c.add(au);
		c.add(year);
		c.add(ye);
		c.add(isbn);
		c.add(is);	
		c.add(b);
		
		b.addActionListener(this);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setSize(425,200);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == b) {
			String query = "";
			String searchAuthor = au.getText();
			String searchTitle = ti.getText();
			String searchYear = ye.getText();
			String searchISBN = is.getText();
			
			String authActive = "";
			String titleActive = "";
			String yearActive = "";
			String isbnActive = "";
			
			int authLength = searchAuthor.trim().length();
			int titLength = searchTitle.trim().length();
			int yearLength = searchYear.trim().length();
			int isbnLength = searchISBN.trim().length();
			
			ArrayList<String> activeFields = new ArrayList<String>();
			
			if(authLength > 0) {
				authActive = "upper(author) like upper('%" +searchAuthor +"%')";
				activeFields.add(authActive);
			}
			if(titLength > 0) {
				titleActive = "upper(title) like upper('%" +searchTitle +"%')";
				activeFields.add(titleActive);
			}
			if(yearLength > 0) {
				yearActive = "extract(year from year) like '%" +searchYear +"%'";
				activeFields.add(yearActive);
			}
			if(isbnLength > 0) {
				isbnActive = "isbn like '%" +searchISBN +"%'";
				activeFields.add(isbnActive);
			}
			
			if(activeFields.size() == 1) {
				query = "select * from tbl_book where " +activeFields.get(0);
			}else if(activeFields.size() > 1) {
				query = "select * from tbl_book where " +activeFields.get(0);
				for(int i = 1; i < activeFields.size(); i++) {
					query += " and " +activeFields.get(i);
				}
			}else {
				System.out.println("Please Enter Author/Title/Year/ISBN");
				return;
			}
			
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				System.out.println("Query is " +query);
				System.out.println();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
				
				int i = 1;
				while(rs.next()) {
					System.out.println("Record " +i);
					System.out.println("-------------------------------------------------------------------------------");
					System.out.println("Isbn: " +rs.getString("isbn")
					+"\nTitle: " +rs.getString("title")
					+"\nAuthor:" +rs.getString("author")
					+"\nPublisher: " +rs.getString("publisher")
					+"\nYear: " +rs.getString("year"));
					System.out.println("-------------------------------------------------------------------------------");
					System.out.println();
					System.out.println();
					i++;
				}
				
				if(i == 1) {
					System.out.println("No Records Found In Database");
				}
				
				rs.close();
				stmt.close();
				conn.close();
			}catch(SQLException ex) {
				System.out.println("Exceptions");
				ex.printStackTrace();
				System.out.println(ex);
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		
		Desktop des = new Desktop();
		des.setTitle("My desktop");
		
	}

}
