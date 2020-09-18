package com.ciit.lp.peer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class SupervisorNode extends Thread{
	public static volatile boolean keepRunning = true;
	Selector sel = null;
	SupervisorNode(Selector sel) {
        this.sel = sel;
    }

	public void shutDown()
	{
		keepRunning = false;
	}
	
    public void run() {
        while (keepRunning) {
            
            // our canned response for now
            ByteBuffer resp = ByteBuffer.wrap(new String("got it\n").getBytes());
            try {
                // loop over all the sockets that are ready for some activity
                while (this.sel.select() > 0) {
                    Set keys = this.sel.selectedKeys();
                    Iterator i = keys.iterator();
                    while (i.hasNext()) {
                        SelectionKey key = (SelectionKey)i.next();
                        if (key.isAcceptable()) {
                            // this means that a new client has hit the port our main
                            // socket is listening on, so we need to accept the  connection
                            // and add the new client socket to our select pool for reading
                            // a command later
                            System.out.println("Accepting connection!");
                            // this will be the ServerSocketChannel we initially registered
                            // with the selector in main()
                            ServerSocketChannel sch = (ServerSocketChannel)key.channel();
                            SocketChannel ch = sch.accept();
                            Runnable r=(Runnable) key.attachment();
                            ch.configureBlocking(false);
                            ch.register(this.sel, SelectionKey.OP_READ);
                        } else if (key.isReadable()) {
                            // one of our client sockets has received a command and
                            // we're now ready to read it in
                            System.out.println("Accepting command!");                            
                            SocketChannel ch = (SocketChannel)key.channel();
                            ByteBuffer buf = ByteBuffer.allocate(200);
                            ch.read(buf);
                            buf.flip();
                            Charset charset = Charset.forName("UTF-8");
                            CharsetDecoder decoder = charset.newDecoder();
                            CharBuffer cbuf = decoder.decode(buf);
                            System.out.print(cbuf.toString());
                            // re-register this socket with the selector, this time
                            // for writing since we'll want to write something to it
                            // on the next go-around
                            ch.register(this.sel, SelectionKey.OP_WRITE);
                        } else if (key.isWritable()) {
                            // we are ready to send a response to one of the client sockets
                            // we had read a command from previously
                            System.out.println("Sending response!");
                            SocketChannel ch = (SocketChannel)key.channel();
                            ch.write(resp);
                            resp.rewind();
                            // we may get another command from this guy, so prepare
                            // to read again. We could also close the channel, but
                            // that sort of defeats the whole purpose of doing async
                            ch.register(this.sel, SelectionKey.OP_READ);
                        }
                        i.remove();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error in poll loop");
                System.out.println(e.getMessage());
                System.exit(1);
            }
            catch(Exception ex)
            {
            	
            }
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
    	// TODO Auto-generated method stub
    	super.finalize();
    	shutDown();
    }
}
