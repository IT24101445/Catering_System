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
    const list = await http('GET', `${api}/assignments`);
    const rows = list.map(a=>`<tr data-id="${a.id}">
      <td>${a.id}</td>
      <td>${a.orderCustomerName||a.orderId||''}</td>
      <td>${a.driverName||a.driverId||''}</td>
      <td><input class="input" value="${a.route||''}" data-field="route" /></td>
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
  function bind(){
    $('#btn-asg-refresh').addEventListener('click', refresh);
    $('#btn-asg-create').addEventListener('click', async ()=>{ await http('POST', `${api}/assignments`, { orderId: +val('asg-orderId'), driverId: +val('asg-driverId'), route: val('asg-route') }); refresh(); });
    document.getElementById('asg-tbody').addEventListener('click', async (e)=>{
      const btn = e.target.closest('button');
      if(!btn) return;
      const tr = btn.closest('tr');
      const id = tr.getAttribute('data-id');
      if(btn.dataset.action === 'delete'){
        await http('DELETE', `${api}/assignments/${id}`);
        refresh();
      } else if(btn.dataset.action === 'update'){
        const data = {};
        tr.querySelectorAll('input[data-field]').forEach(inp=>{ const v = inp.value.trim(); if(v) data[inp.dataset.field]=v; });
        await http('PUT', `${api}/assignments/${id}`, data);
        refresh();
      } else if(btn.dataset.action === 'start'){
        await http('POST', `${api}/assignments/${id}/start`);
        refresh();
      } else if(btn.dataset.action === 'complete'){
        await http('POST', `${api}/assignments/${id}/complete`);
        refresh();
      } else if(btn.dataset.action === 'reassign'){
        const newDriverId = prompt('New driver ID?'); if(!newDriverId) return;
        await http('POST', `${api}/assignments/${id}/reassign`, { newDriverId: +newDriverId });
        refresh();
      }
    });
  }
  window.addEventListener('DOMContentLoaded', ()=>{ bind(); refresh(); });
})();


