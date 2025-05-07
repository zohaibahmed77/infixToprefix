let history = [];
let isProcessVisible = false;

function convertExpression() {
  const input = document.getElementById("infixInput").value.trim();
  const output = document.getElementById("prefixOutput");
  const errorPopup = document.getElementById("fullscreenError");
  const thinkingPopup = document.getElementById("fullscreenThinking");

  if (!input) {
    output.innerText = "Please enter an expression.";
    output.style.color = "red";
    showPopup(errorPopup);
    return;
  }

  fetch(`/api/convert?expression=${encodeURIComponent(input)}`)
    .then(res => res.json())
    .then(data => {
      const prefix = data.result;
      const steps = data.steps || [];

      if (prefix === "Invalid Expression!") {
        output.innerText = prefix;
        output.style.color = "red";
        showPopup(errorPopup);
        renderProcessTable([]);
        return;
      }

      showPopup(thinkingPopup);

      setTimeout(() => {
        output.innerText = prefix;
        output.style.color = "green";
        history.push({ infix: input, prefix, steps });
        updateHistory();
        renderProcessTable(steps);
      }, 1500);
    })
    .catch(err => {
      output.innerText = "Error connecting to backend.";
      output.style.color = "red";
      showPopup(errorPopup);
    });
}

function showPopup(popup) {
  document.body.classList.add("popup-active");
  document.querySelectorAll(".fullscreen-popup").forEach(p => p.classList.add("hidden"));
  popup.classList.remove("hidden");

  setTimeout(() => {
    popup.classList.add("hidden");
    document.body.classList.remove("popup-active");
  }, 2000);
}

function updateHistory() {
  const table = document.getElementById("historyTable");
  table.innerHTML = "";
  history.slice().reverse().forEach((item, index) => {
    const row = table.insertRow();
    row.insertCell(0).innerText = item.infix;
    row.insertCell(1).innerText = item.prefix;

    const btnCell = row.insertCell(2);
    const btn = document.createElement("button");
    btn.innerText = "View Process";
    btn.onclick = () => {
      isProcessVisible = true;
      document.getElementById("processTableSection").classList.remove("hidden");
      renderProcessTable(item.steps);
    };
    btnCell.appendChild(btn);
  });
}

function copyResult() {
  const result = document.getElementById("prefixOutput").innerText;
  if (result && result !== "---" && result !== "Invalid Expression!") {
    navigator.clipboard.writeText(result);
    alert("Copied to clipboard!");
  }
}

function clearFields() {
  document.getElementById("infixInput").value = "";
  document.getElementById("prefixOutput").innerText = "---";
  document.getElementById("prefixOutput").style.color = "";
  document.getElementById("processTable").querySelector("tbody").innerHTML = "";
  isProcessVisible = false;
  document.getElementById("processTableSection").classList.add("hidden");
  history = [];
  document.getElementById("historyTable").innerHTML = "";
}

function toggleTheme() {
  document.body.classList.toggle("dark-mode");
}

function toggleProcess() {
  isProcessVisible = !isProcessVisible;
  document.getElementById("processTableSection").classList.toggle("hidden", !isProcessVisible);
}

function renderProcessTable(steps) {
  const tableBody = document.getElementById("processTable").querySelector("tbody");
  tableBody.innerHTML = "";

  if (!steps.length) {
    const row = tableBody.insertRow();
    const cell = row.insertCell(0);
    cell.colSpan = 4;
    cell.innerText = "No steps to display.";
    cell.style.textAlign = "center";
    return;
  }

  steps.forEach((s, index) => {
    const row = tableBody.insertRow();
    row.insertCell(0).innerText = s.step ?? index + 1;
    row.insertCell(1).innerText = s.symbol;
    row.insertCell(2).innerText = s.stack;
    row.insertCell(3).innerText = s.output;
  });
}
