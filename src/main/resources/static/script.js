const API = "/api";


// ==========================================
// LOAD DASHBOARD
// ==========================================

async function loadDashboard() {

    try {

        await Promise.all([
            loadHouseholds(),
            loadWorkers(),
            loadZones(),
            loadCollections(),
            loadZoneWaste(),
            loadAboveAverage()
        ]);

    } catch (error) {

        console.error("Dashboard loading error:", error);

    }
}


// ==========================================
// HOUSEHOLDS
// ==========================================

async function loadHouseholds() {

    const response =
        await fetch(`${API}/households`);

    const households =
        await response.json();

    document.getElementById("householdCount").textContent =
        households.length;
}


// ==========================================
// WORKERS
// ==========================================

async function loadWorkers() {

    const response =
        await fetch(`${API}/workers`);

    const workers =
        await response.json();

    document.getElementById("workerCount").textContent =
        workers.length;
}


// ==========================================
// ZONES
// ==========================================

async function loadZones() {

    const response =
        await fetch(`${API}/zones`);

    const zones =
        await response.json();

    document.getElementById("zoneCount").textContent =
        zones.length;
}


// ==========================================
// COLLECTIONS
// ==========================================

async function loadCollections() {

    const response =
        await fetch(`${API}/collections`);

    const collections =
        await response.json();

    document.getElementById("collectionCount").textContent =
        collections.length;

    const table =
        document.getElementById("collectionsTable");

    table.innerHTML = "";

    collections
        .slice()
        .reverse()
        .forEach(collection => {

            const row =
                document.createElement("tr");

            const statusClass =
                collection.status === "Collected"
                    ? "status-collected"
                    : "status-scheduled";

            row.innerHTML = `
                <td>${collection.collectionId}</td>

                <td>
                    ${collection.householdId}
                </td>

                <td>
                    ${collection.workerId}
                </td>

                <td>
                    ${collection.zoneId}
                </td>

                <td>
                    ${collection.collectionDate}
                </td>

                <td>
                    ${collection.wasteVolume}
                </td>

                <td>
                    <span class="status ${statusClass}">
                        ${collection.status}
                    </span>
                </td>
            `;

            table.appendChild(row);
        });
}


// ==========================================
// TOTAL WASTE BY ZONE
// ==========================================

async function loadZoneWaste() {

    const container =
        document.getElementById("zoneWasteContainer");

    container.innerHTML = "";

    for (let zoneId = 1; zoneId <= 4; zoneId++) {

        try {

            const response =
                await fetch(
                    `${API}/collections/function/total-waste/${zoneId}`
                );

            const waste =
                await response.json();

            const card =
                document.createElement("div");

            card.className = "zone-card";

            card.innerHTML = `
                <h3>Zone ${zoneId}</h3>

                <div class="waste">
                    ${Number(waste).toFixed(2)}
                </div>

                <span>
                    Total Waste
                </span>
            `;

            container.appendChild(card);

        } catch (error) {

            console.error(
                `Error loading Zone ${zoneId}:`,
                error
            );
        }
    }
}


// ==========================================
// ABOVE AVERAGE ZONES
// ==========================================

async function loadAboveAverage() {

    const response =
        await fetch(
            `${API}/collections/above-average`
        );

    const data =
        await response.json();

    const container =
        document.getElementById(
            "aboveAverageContainer"
        );

    container.innerHTML = "";

    if (data.length === 0) {

        container.innerHTML =
            "<p>No zones are above the average.</p>";

        return;
    }

    data.forEach(item => {

        const zoneName =
            item[0];

        const totalWaste =
            item[1];

        const div =
            document.createElement("div");

        div.className =
            "average-item";

        div.innerHTML = `
            <strong>
                ${zoneName}
            </strong>

            <span>
                ${Number(totalWaste).toFixed(2)} kg
            </span>
        `;

        container.appendChild(div);
    });
}


// ==========================================
// SCHEDULE COLLECTION
// ==========================================

document
    .getElementById("collectionForm")
    .addEventListener("submit", async function(event) {

        event.preventDefault();

        const householdId =
            Number(
                document.getElementById(
                    "householdId"
                ).value
            );

        const workerId =
            Number(
                document.getElementById(
                    "workerId"
                ).value
            );

        const zoneId =
            Number(
                document.getElementById(
                    "zoneId"
                ).value
            );

        const collectionDate =
            document.getElementById(
                "collectionDate"
            ).value;

        const wasteVolume =
            Number(
                document.getElementById(
                    "wasteVolume"
                ).value
            );


        const message =
            document.getElementById(
                "formMessage"
            );

        message.textContent =
            "Scheduling collection...";

        message.style.color =
            "#166534";


        try {

            const response =
                await fetch(
                    `${API}/collections/procedure`,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({

                            householdId:
                                householdId,

                            workerId:
                                workerId,

                            zoneId:
                                zoneId,

                            collectionDate:
                                collectionDate,

                            wasteVolume:
                                wasteVolume
                        })
                    }
                );


            if (!response.ok) {

                throw new Error(
                    "Failed to schedule collection"
                );
            }


            const result =
                await response.text();


            message.textContent =
                result;

            message.style.color =
                "#15803d";


            document
                .getElementById(
                    "collectionForm"
                )
                .reset();


            await loadDashboard();


        } catch (error) {

            console.error(error);

            message.textContent =
                "Error scheduling collection.";

            message.style.color =
                "#dc2626";
        }

    });


// ==========================================
// START DASHBOARD
// ==========================================

document.addEventListener(
    "DOMContentLoaded",
    () => {

        loadDashboard();

    }
);