import java.io.*;
import java.util.*;
import java.net.*;


class Record
{
	ArrayList <String> client_name = new ArrayList <String>(100);
	ArrayList <Integer> client_socket = new ArrayList <Integer>(100);
	Socket []s = new Socket[100];
	just_write []jw = new just_write[100];
}

class just_write
{
	Record r;
	synchronized void write_to(DataOutputStream dw1,String client_name,String str) throws IOException
	{
		System.out.println("client_name "+client_name+" message "+str);
		dw1.writeUTF(client_name+" :\n");
		dw1.writeUTF("  "+str+"\n");	
	}
}

class Chat
{
	volatile String str = "";
	volatile String recipient;
	volatile String client_name;
			
}

class thread extends Thread
{
	Socket s0 = null;							//to communicate with intial client..dw0	
	int client_number;
	char read_write;
	Record r;
	Chat ch;
	DataOutputStream dw0 = null;;
	DataOutputStream dw1 = null;
	DataInputStream dr = null;
	int recipient_socket;
	
	thread(Socket s,int number,char rw,Record r,Chat ch)
	{
		this.s0 = s;
		client_number = number;		
		read_write = rw;
		this.ch = ch;
		this.r = r;
		start();
	}
	public void run()
	{
		
		try
		{
			if(read_write=='w')
			{
				
				dw0 = new DataOutputStream(s0.getOutputStream());      // to know the name of client..
				dw0.writeUTF("-- Enter your name to communicate with other.. \n\n -- thenEnter your recipient name with whom you to communicate.. \n\n -- to change recipient just write 'change recipient'..then specify his/her name");
				
				
				while(true)
				{
					while(ch.str == "" || (ch.str).equals("change recipient"));
					try
					{												
						recipient_socket = r.client_socket.get(r.client_name.indexOf(ch.recipient));
						dw1 = new DataOutputStream(r.s[recipient_socket].getOutputStream());
						r.jw[recipient_socket].write_to(dw1,ch.client_name,ch.str);
						
						
					}
					
					catch(Exception e){
					
						System.out.println("recipient may be offline..try another one..");
						dw0.writeUTF("recipient may be offline..try another one..");
						e.printStackTrace();
						System.out.println("error...."+e.getCause());
						
						}
					finally
					{
						ch.str = "";
					}
					
				}	
			}
			else
			{
				dr = new DataInputStream(s0.getInputStream());
				ch.client_name = dr.readUTF();				
				r.client_name.add(ch.client_name);		
				r.client_socket.add(client_number);
				while(true)
				{
					ch.recipient = dr.readUTF();
					ch.str = dr.readUTF();
					while(!((ch.str).equals("change recipient")))
					{
						ch.str = dr.readUTF();						
					}	
					
				}	
				
			}
						
			
		}
		catch(Exception e)
		{
						
			System.out.println("recipient may be offline..");
			
		}
		finally 
		{
			try
			{
				if(s0!=null)
					s0.close();
				if(dw0!=null)
					dw0.close();
				if(dw1!=null)
					dw1.close();
				if(dr!=null)
					dr.close();
			}
			catch(Exception e){}
		}
	}
}	
class server
{
	public static void main(String []args) throws Exception
	{
		ServerSocket sc = new ServerSocket(11);
		thread []tr = new thread[100];
		thread []tw = new thread[100];
		Record r = new Record();
		Chat ch = null;
		int client_count=0;
		while(true)
		{
		
			ch = new Chat();		
			r.s[client_count] = sc.accept();
			System.out.println("server side write "+r.s[client_count]);
			r.jw[client_count] = new just_write();
			tr[client_count] = new thread(r.s[client_count],client_count,'r',r,ch);
			tw[client_count] = new thread(r.s[client_count],client_count,'w',r,ch);				
			client_count++;
		}
	}
}	
