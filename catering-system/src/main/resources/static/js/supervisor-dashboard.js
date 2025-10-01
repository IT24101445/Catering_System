(function () {
    const api = '/api';

    function $(sel) { return document.querySelector(sel); }
    function val(id) { return document.getElementById(id).value.trim(); }
    function setRows(tbodyId, rowsHtml) { document.getElementById(tbodyId).innerHTML = rowsHtml; }

    // Tabs
    function initTabs() {
        const tabs = document.querySelectorAll('.tab');
        tabs.forEach(t => t.addEventListener('click', () => {
            tabs.forEach(x => x.classList.remove('tab-active'));
            t.classList.add('tab-active');
            const target = t.getAttribute('data-tab');
            document.querySelectorAll('.tab-panel').forEach(p => p.classList.add('hidden'));
            document.getElementById(`tab-${target}`).classList.remove('hidden');
        }));
    }

    // Fetch helpers
    async function http(method, url, body) {
        const opts = { method, headers: { 'Content-Type': 'application/json' } };
        if (body) opts.body = JSON.stringify(body);
        const res = await fetch(url, opts);
        if (!res.ok) throw new Error(await res.text());
        const ct = res.headers.get('content-type') || '';
        return ct.includes('application/json') ? res.json() : null;
    }

    // Drivers
    async function refreshDrivers() {
        const list = await http('GET', `${api}/drivers`);
        const rows = list.map(d => `<tr data-id="${d.id}">
            <td>${d.id}</td>
            <td><input class="input" value="${d.name || ''}" data-field="name" /></td>
            <td><input class="input" value="${d.email || ''}" data-field="email" /></td>
            <td><input class="input" value="${d.status || ''}" data-field="status" /></td>
            <td><button class="btn" data-action="update">Update</button> <button class="btn" data-action="delete">Remove</button></td>
        </tr>`).join('');
        setRows('drv-tbody', rows);
    }
    function bindDrivers() {
        $('#btn-drv-refresh').addEventListener('click', refreshDrivers);
        $('#btn-drv-create').addEventListener('click', async () => {
            await http('POST', `${api}/drivers`, { email: val('drv-email'), name: val('drv-name'), status: val('drv-status') || undefined });
            refreshDrivers();
        });
        document.getElementById('drv-tbody').addEventListener('click', async (e) => {
            const btn = e.target.closest('button');
            if (!btn) return;
            const tr = btn.closest('tr');
            const id = tr.getAttribute('data-id');
            if (btn.dataset.action === 'delete') {
                await http('DELETE', `${api}/drivers/${id}`);
                refreshDrivers();
            } else if (btn.dataset.action === 'update') {
                const data = {};
                tr.querySelectorAll('input[data-field]').forEach(inp => { const v = inp.value.trim(); if (v) data[inp.dataset.field] = v; });
                await http('PUT', `${api}/drivers/${id}`, data);
                refreshDrivers();
            }
        });
    }

    // Orders
    async function refreshOrders() {
        const list = await http('GET', `${api}/orders`);
        const rows = list.map(o => `<tr data-id="${o.id}">
            <td>${o.id}</td>
            <td><input class="input" value="${o.customerName || ''}" data-field="customerName" /></td>
            <td><input class="input" value="${o.pickupAddress || ''}" data-field="pickupAddress" /></td>
            <td><input class="input" value="${o.dropoffAddress || ''}" data-field="dropoffAddress" /></td>
            <td><button class="btn" data-action="update">Update</button> <button class="btn" data-action="delete">Remove</button></td>
        </tr>`).join('');
        setRows('ord-tbody', rows);
    }
    function bindOrders() {
        $('#btn-ord-refresh').addEventListener('click', refreshOrders);
        $('#btn-ord-create').addEventListener('click', async () => {
            await http('POST', `${api}/orders`, { customerName: val('ord-customer'), pickupAddress: val('ord-pickup'), dropoffAddress: val('ord-dropoff') });
            refreshOrders();
        });
        document.getElementById('ord-tbody').addEventListener('click', async (e) => {
            const btn = e.target.closest('button');
            if (!btn) return;
            const tr = btn.closest('tr');
            const id = tr.getAttribute('data-id');
            if (btn.dataset.action === 'delete') {
                await http('DELETE', `${api}/orders/${id}`);
                refreshOrders();
            } else if (btn.dataset.action === 'update') {
                const data = {};
                tr.querySelectorAll('input[data-field]').forEach(inp => { const v = inp.value.trim(); if (v) data[inp.dataset.field] = v; });
                await http('PUT', `${api}/orders/${id}`, data);
                refreshOrders();
            }
        });
    }

    // Deliveries
    async function refreshDeliveries() {
        const list = await http('GET', `${api}/deliveries`);
        const rows = list.map(d => `<tr data-id="${d.id}">
            <td>${d.id}</td>
            <td><input class="input" value="${d.pickupAddress || ''}" data-field="pickupAddress" /></td>
            <td><input class="input" value="${d.dropoffAddress || ''}" data-field="dropoffAddress" /></td>
            <td><input class="input" value="${d.status || ''}" data-field="status" /></td>
            <td>${d.directionsUrl ? `<a href="${d.directionsUrl}" target="_blank">Open</a>` : ''}</td>
            <td><button class="btn" data-action="update">Update</button> <button class="btn" data-action="delete">Remove</button></td>
        </tr>`).join('');
        setRows('del-tbody', rows);
    }
    function bindDeliveries() {
        $('#btn-del-refresh').addEventListener('click', refreshDeliveries);
        $('#btn-del-create').addEventListener('click', async () => {
            await http('POST', `${api}/deliveries`, { pickupAddress: val('del-pickup'), dropoffAddress: val('del-dropoff'), status: val('del-status') || undefined });
            refreshDeliveries();
        });
        document.getElementById('del-tbody').addEventListener('click', async (e) => {
            const btn = e.target.closest('button');
            if (!btn) return;
            const tr = btn.closest('tr');
            const id = tr.getAttribute('data-id');
            if (btn.dataset.action === 'delete') {
                await http('DELETE', `${api}/deliveries/${id}`);
                refreshDeliveries();
            } else if (btn.dataset.action === 'update') {
                const data = {};
                tr.querySelectorAll('input[data-field]').forEach(inp => { const v = inp.value.trim(); if (v) data[inp.dataset.field] = v; });
                await http('PUT', `${api}/deliveries/${id}`, data);
                refreshDeliveries();
            }
        });
    }

    // Assignments
    async function refreshAssignments() {
        const list = await http('GET', `${api}/assignments`);
        const rows = list.map(a => `<tr data-id="${a.id}">
            <td>${a.id}</td><td>${a.orderCustomerName || a.orderId || ''}</td><td>${a.driverName || a.driverId || ''}</td>
            <td><input class="input" value="${a.route || ''}" data-field="route" /></td>
            <td>
                <button class="btn" data-action="update">Update</button>
                <button class="btn" data-action="delete">Remove</button>
                <button class="btn" data-action="start">Start</button>
                <button class="btn" data-action="complete">Complete</button>
                <button class="btn" data-action="reassign">Reassign</button>
            </td>
        </tr>`).join('');
        setRows('asg-tbody', rows);
    }
    function bindAssignments() {
        $('#btn-asg-refresh').addEventListener('click', refreshAssignments);
        $('#btn-asg-create').addEventListener('click', async () => {
            await http('POST', `${api}/assignments`, { orderId: +val('asg-orderId'), driverId: +val('asg-driverId'), route: val('asg-route') });
            refreshAssignments();
        });
        document.getElementById('asg-tbody').addEventListener('click', async (e) => {
            const btn = e.target.closest('button');
            if (!btn) return;
            const tr = btn.closest('tr');
            const id = tr.getAttribute('data-id');
            if (btn.dataset.action === 'delete') {
                await http('DELETE', `${api}/assignments/${id}`);
                refreshAssignments();
            } else if (btn.dataset.action === 'update') {
                const data = {};
                tr.querySelectorAll('input[data-field]').forEach(inp => { const v = inp.value.trim(); if (v) data[inp.dataset.field] = v; });
                await http('PUT', `${api}/assignments/${id}`, data);
                refreshAssignments();
            } else if (btn.dataset.action === 'start') {
                await http('POST', `${api}/assignments/${id}/start`);
                refreshAssignments();
            } else if (btn.dataset.action === 'complete') {
                await http('POST', `${api}/assignments/${id}/complete`);
                refreshAssignments();
            } else if (btn.dataset.action === 'reassign') {
                const newDriverId = prompt('New driver ID?');
                if (!newDriverId) return;
                await http('POST', `${api}/assignments/${id}/reassign`, { newDriverId: +newDriverId });
                refreshAssignments();
            }
        });
    }

    function init() {
        initTabs();
        bindDrivers();
        bindOrders();
        bindDeliveries();
        bindAssignments();
        // initial loads
        refreshDrivers();
        refreshOrders();
        refreshDeliveries();
        refreshAssignments();
    }

    window.addEventListener('DOMContentLoaded', init);
})();


