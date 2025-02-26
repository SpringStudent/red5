package org.red5.server.plugin.admin.stats;

/*
 * RED5 Open Source Flash Server - http://www.osflash.org/red5
 * 
 * Copyright (c) 2006-2008 by respective authors (see below). All rights reserved.
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.ScopeUtils;
import org.red5.server.plugin.admin.utils.Utils;

/**
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Martijn van Beek (martijn.vanbeek@gmail.com)
 */
public class UserStatistics {
	private HashMap<Integer, HashMap<String, String>> apps;

	private int id;

	public HashMap<Integer, HashMap<String, String>> getStats(String userid, IScope scope) {
		apps = new HashMap<Integer, HashMap<String, String>>();
		id = 0;
		IScope root = ScopeUtils.findRoot(scope);
		Set<IClient> clients = root.getClients();
		Iterator<IClient> client = clients.iterator();
		extractConnectionData(root);
		addData("User attributes", "--");
		while (client.hasNext()) {
			IClient c = client.next();
			if (c.getId().equals(userid)) {
				Set<String> names = c.getAttributeNames();
				Iterator<String> itnames = names.iterator();
				while (itnames.hasNext()) {
					String key = itnames.next();
					addData(key, c.getAttribute(key));
				}
				addData("Created", Utils.formatDate(c.getCreationTime()));
			}
		}
		return apps;
	}

	protected void addData(String name, Object value) {
		HashMap<String, String> app = new HashMap<String, String>();
		app.put("name", name);
		app.put("value", value.toString());
		apps.put(id, app);
		id++;
	}

	protected void extractConnectionData(IScope root) {

		Collection<Set<IConnection>> conns = root.getConnections();
		
		for (Set<IConnection> set : conns) {
			for (IConnection connection : set) {
				addData("Scope statistics", "--");
				addData("Send bytes", Utils.formatBytes(connection
						.getWrittenBytes()));
				addData("Received bytes", Utils.formatBytes(connection
						.getReadBytes()));
				addData("Send messages", connection.getWrittenMessages());
				addData("Received messages", connection.getReadMessages());
				addData("Dropped messages", connection.getDroppedMessages());
				addData("Pending messages", connection.getPendingMessages());
				addData("Remote address", connection.getRemoteAddress() + ":"
						+ connection.getRemotePort() + " (" + connection.getHost()
						+ ")");
				addData("Path", connection.getPath());
			}
		}
	}
}