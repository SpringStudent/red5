package org.red5.server.stream;

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 *
 * Copyright (c) 2006-2009 by respective authors. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;

import org.apache.mina.core.buffer.IoBuffer;
import org.red5.server.net.rtmp.event.VideoData;
import org.red5.server.stream.message.RTMPMessage;

/**
 * TODO: extend testcase
 * 
 * @author m.j.milicevic <marijan at info.nl>
 */
public class PlayBufferTest extends TestCase {
	public static Test suite() {
		return new JUnit4TestAdapter(PlayBufferTest.class);
	}

	PlayBuffer playBuffer;

	private RTMPMessage rtmpMessage;

	private void dequeue() throws Exception {
		setUp();
	}

	/**
	 * enqueue with messages
	 */
	private void enqueue() {
		boolean success = playBuffer.putMessage(rtmpMessage);
		assertTrue("message successfully put into play buffer", success);
	}

	/** {@inheritDoc} */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		VideoData message = new VideoData(IoBuffer.allocate(100));
		playBuffer = new PlayBuffer(1000);
		rtmpMessage = new RTMPMessage();
		rtmpMessage.setBody(message);
	}

	public void testClear() {
		enqueue();
		playBuffer.clear();
		assertTrue(playBuffer.getMessageCount() == 0);
	}

	public void testPeekMessage() throws Exception {
		enqueue();
		assertTrue(playBuffer.peekMessage().equals(rtmpMessage));
		dequeue();
	}

	public void testPlayBuffer() {
		assertTrue("player buffer should be initialized", playBuffer != null);
	}

	public void testPutMessage() throws Exception {
		enqueue();
		RTMPMessage peek_message = playBuffer.peekMessage();
		assertNotNull("message shouldn't be null", peek_message);
		assertTrue(peek_message.equals(rtmpMessage));
		dequeue();
	}

	public void testTakeMessage() throws Exception {
		enqueue();
		assertTrue(playBuffer.takeMessage().equals(rtmpMessage));
		dequeue();
	}

}