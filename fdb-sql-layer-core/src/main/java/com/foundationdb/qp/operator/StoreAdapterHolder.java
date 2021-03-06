/**
 * Copyright (C) 2009-2013 FoundationDB, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.foundationdb.qp.operator;

import com.foundationdb.ais.model.Table;
import com.foundationdb.qp.virtualadapter.VirtualAdapter;
import com.foundationdb.server.service.config.ConfigurationService;
import com.foundationdb.server.service.session.Session;
import com.foundationdb.server.store.Store;

public class StoreAdapterHolder
{
    private StoreAdapter storeAdapter;
    private StoreAdapter virtualAdapter;

    public StoreAdapterHolder() {
    }

    public void init(Session session, ConfigurationService config, Store store) {
        storeAdapter = store.createAdapter(session);
        virtualAdapter = new VirtualAdapter(session, config);
    }

    public StoreAdapter getAdapter() {
        return storeAdapter;
    }

    public StoreAdapter getAdapter(Table table) {
        return table.isVirtual() ? virtualAdapter : storeAdapter;
    }
}
