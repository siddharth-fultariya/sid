import java.io.*;
//import java.util.Scanner;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

class gui 
{
	JFrame j;
	JTextArea ja_s;
	JTextField jf;
	JTextArea ja_c;
	JButton b;
	JScrollPane jp;
	gui()
	{
		j = new JFrame();
		ja_s = new JTextArea();
		ja_c = new JTextArea();
		jp = new JScrollPane(ja_c);
		jp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		ja_s.setLineWrap(true);
		ja_s.setWrapStyleWord(true);
		ja_c.setLineWrap(true);
		ja_c.setWrapStyleWord(true);
		jf = new JTextField();
		b = new JButton("send");
		j.setLayout(null);
		jf.setBounds(10,10,100,30);
		ja_s.setBounds(310,10,190,200);
		jp.setBounds(10,50,280,200);
		b.setBounds(120,10,100,25);
		j.add(jf);
		j.add(ja_s);
		j.add(b);
		j.add(jp);
		j.setVisible(true);
		j.setSize(600,400);
		j.setTitle("client log..");
	}
}
class thread extends Thread
{	
	gui g;
	char data;
	
	Socket s;thread(char data,Socket s,gui g)
	{
		this.data = data;
		this.s = s;
		this.g = g;
		start();
	}
	
	public void run()
	{
		//Scanner sc;
		try{
				
				if(data=='w')
				{
					g.b.addActionListener(new myclass());
					while(true)
					{	
						//sc = new Scanner(System.in);
						//System.out.println("client write");
						
					}
				}	
				else
				{
					String data_s = "";
					String data_c = "";	
					//System.out.println("client read");
					DataInputStream dr = new DataInputStream(s.getInputStream());
					data_s+= dr.readUTF();
					System.out.println(data_s);
					g.ja_s.setText(data_s);
					while(true)
					{
						data_c+= dr.readUTF();
						g.ja_c.setText(data_c);
					}	
					
				}
			}
		
		catch(Exception e){}
	}	
	public class myclass implements ActionListener
	{
		public void actionPerformed(ActionEvent ae) 
		{
			try
			{
				DataOutputStream dw;
				dw = new DataOutputStream(s.getOutputStream());
				if((g.jf.getText()).length()>0)
				{
					//System.out.println("empty message..");
					dw.writeUTF(g.jf.getText());
					g.jf.setText("");
				}	
			}
			catch(Exception aee){}
		}
	}
}	
class client
{
	public static void main(String []args) throws Exception
	{
		Socket s = new Socket("localhost",11);
		gui g = new gui();
		thread tw = new thread('w',s,g);
		thread tr = new thread('r',s,g);
		tw.join();
		tr.join();	
		s.close();
		System.out.println("bye server");
	}
}	