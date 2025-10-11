(function(){
  const api = '/api';
  function $(sel){ return document.querySelector(sel); }
  function val(id){ return document.getElementById(id).value.trim(); }
  function setRows(id, html){ document.getElementById(id).innerHTML = html; }
  async function http(method, url, body){
    const res = await fetch(url, { method, headers: { 'Content-Type': 'application/json' }, body: body ? JSON.stringify(body) : undefined });
    if(!res.ok) throw new Error(await res.text());
    const ct = res.headers.get('content-type')||'';
    return ct.includes('application/json')? res.json(): null;
  }
  async function refresh(){
    const list = await http('GET', `${api}/drivers`);
    const rows = list.map(d=>`<tr data-id="${d.id}">
      <td>${d.id}</td>
      <td><input class="input" value="${d.name||''}" data-field="name" /></td>
      <td><input class="input" value="${d.email||''}" data-field="email" /></td>
      <td>
        <select class="input" data-field="status">
          <option value="AVAILABLE" ${d.status === 'AVAILABLE' ? 'selected' : ''}>Available</option>
          <option value="ON_ROUTE" ${d.status === 'ON_ROUTE' ? 'selected' : ''}>On Route</option>
        </select>
      </td>
      <td>
        <button class="btn" data-action="update">Update</button>
        <button class="btn" data-action="delete">Remove</button>
      </td>
    </tr>`).join('');
    setRows('drv-tbody', rows);
  }
  function bind(){
    $('#btn-drv-refresh').addEventListener('click', refresh);
    $('#btn-drv-create').addEventListener('click', async ()=>{ 
      await http('POST', `${api}/drivers`, { 
        email: val('drv-email'), 
        name: val('drv-name'), 
        password: val('drv-password'),
        status: val('drv-status')||undefined 
      }); 
      refresh(); 
    });
    document.getElementById('drv-tbody').addEventListener('click', async (e)=>{
      const btn = e.target.closest('button');
      if(!btn) return;
      const tr = btn.closest('tr');
      const id = tr.getAttribute('data-id');
      if(btn.dataset.action === 'delete'){
        await http('DELETE', `${api}/drivers/${id}`);
        refresh();
      } else if(btn.dataset.action === 'update'){
        const data = {};
        tr.querySelectorAll('input[data-field]').forEach(inp=>{ const v = inp.value.trim(); if(v) data[inp.dataset.field]=v; });
        await http('PUT', `${api}/drivers/${id}`, data);
        refresh();
      }
    });
  }
  async function refreshAssignments(){
    const list = await http('GET', `${api}/assignments`);
    const rows = list.map(a=>`<tr data-id="${a.id}">
      <td>${a.id}</td>
      <td>${a.driverName || a.driverId || ''}</td>
      <td>${a.orderCustomerName || a.orderId || ''}</td>
      <td><input class="input" value="${a.route||''}" data-field="route" /></td>
      <td><span class="status-badge status-${a.status?.toLowerCase() || 'pending'}">${a.status || 'PENDING'}</span></td>
      <td>
        <button class="btn" data-action="update">Update</button>
        <button class="btn" data-action="delete">Remove</button>
      </td>
    </tr>`).join('');
    setRows('assignments-tbody', rows);
  }
  function bindAssignments(){
    $('#btn-assignments-refresh').addEventListener('click', refreshAssignments);
    document.getElementById('assignments-tbody').addEventListener('click', async (e)=>{
      const btn = e.target.closest('button');
      if(!btn) return;
      const tr = btn.closest('tr');
      const id = tr.getAttribute('data-id');
      if(btn.dataset.action === 'delete'){
        await http('DELETE', `${api}/assignments/${id}`);
        refreshAssignments();
      } else if(btn.dataset.action === 'update'){
        const data = {};
        tr.querySelectorAll('input[data-field]').forEach(inp=>{ const v = inp.value.trim(); if(v) data[inp.dataset.field]=v; });
        await http('PUT', `${api}/assignments/${id}`, data);
        refreshAssignments();
      }
    });
  }
  window.addEventListener('DOMContentLoaded', ()=>{ bind(); bindAssignments(); refresh(); refreshAssignments(); });
})();


