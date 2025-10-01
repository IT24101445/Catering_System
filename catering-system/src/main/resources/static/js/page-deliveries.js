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
    const list = await http('GET', `${api}/deliveries`);
    const rows = list.map(d=>`<tr data-id="${d.id}">
      <td>${d.id}</td>
      <td><input class="input" value="${d.pickupAddress||''}" data-field="pickupAddress" /></td>
      <td><input class="input" value="${d.dropoffAddress||''}" data-field="dropoffAddress" /></td>
      <td><input class="input" value="${d.status||''}" data-field="status" /></td>
      <td>${d.directionsUrl?`<a href=\"${d.directionsUrl}\" target=\"_blank\">Open</a>`:''}</td>
      <td>
        <button class="btn" data-action="update">Update</button>
        <button class="btn" data-action="delete">Remove</button>
      </td>
    </tr>`).join('');
    setRows('del-tbody', rows);
  }
  function bind(){
    $('#btn-del-refresh').addEventListener('click', refresh);
    $('#btn-del-create').addEventListener('click', async ()=>{ await http('POST', `${api}/deliveries`, { pickupAddress: val('del-pickup'), dropoffAddress: val('del-dropoff'), status: val('del-status')||undefined }); refresh(); });
    document.getElementById('del-tbody').addEventListener('click', async (e)=>{
      const btn = e.target.closest('button');
      if(!btn) return;
      const tr = btn.closest('tr');
      const id = tr.getAttribute('data-id');
      if(btn.dataset.action === 'delete'){
        await http('DELETE', `${api}/deliveries/${id}`);
        refresh();
      } else if(btn.dataset.action === 'update'){
        const data = {};
        tr.querySelectorAll('input[data-field]').forEach(inp=>{ const v = inp.value.trim(); if(v) data[inp.dataset.field]=v; });
        await http('PUT', `${api}/deliveries/${id}`, data);
        refresh();
      }
    });
  }
  window.addEventListener('DOMContentLoaded', ()=>{ bind(); refresh(); });
})();


