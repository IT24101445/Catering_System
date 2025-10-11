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
    const list = await http('GET', `${api}/orders`);
    const rows = list.map(o=>`<tr data-id="${o.id}">
      <td>${o.id}</td>
      <td><input class="input" value="${o.customerName||''}" data-field="customerName" /></td>
      <td><input class="input" value="${o.pickupAddress||''}" data-field="pickupAddress" /></td>
      <td><input class="input" value="${o.dropoffAddress||''}" data-field="dropoffAddress" /></td>
      <td>
        <button class="btn" data-action="update">Update</button>
        <button class="btn" data-action="delete">Remove</button>
      </td>
    </tr>`).join('');
    setRows('ord-tbody', rows);
  }
  function bind(){
    $('#btn-ord-refresh').addEventListener('click', refresh);
    $('#btn-ord-create').addEventListener('click', async ()=>{ await http('POST', `${api}/orders`, { customerName: val('ord-customer'), pickupAddress: val('ord-pickup'), dropoffAddress: val('ord-dropoff') }); refresh(); });
    document.getElementById('ord-tbody').addEventListener('click', async (e)=>{
      const btn = e.target.closest('button');
      if(!btn) return;
      const tr = btn.closest('tr');
      const id = tr.getAttribute('data-id');
      if(btn.dataset.action === 'delete'){
        await http('DELETE', `${api}/orders/${id}`);
        refresh();
      } else if(btn.dataset.action === 'update'){
        const data = {};
        tr.querySelectorAll('input[data-field]').forEach(inp=>{ const v = inp.value.trim(); if(v) data[inp.dataset.field]=v; });
        await http('PUT', `${api}/orders/${id}`, data);
        refresh();
      }
    });
  }
  window.addEventListener('DOMContentLoaded', ()=>{ bind(); refresh(); });
})();


