
/*
    * -------------------------------------------------------------------------------------------
    * Insert a specified number of patients with random data
*/ 
function insertPatients(dbName, colName, patientCount) {
    const names = ["Palesa Mohlare", 
                    "John Doe", 
                    "Lerato Khumalo", 
                    "Sara Smith",
                     "Kwame Mensah", 
                    "Thabo Dlamini", 
                    "Zanele Mokoena", 
                    "Sipho Ndlovu", 
                    "Ayanda Nkosi", 
                    "Nomsa Khumalo"];

    const ailments = ["Cold", "Flu", "Allergy", "Stomach Ache", "Headache"];

    const doctors = ["Dr. Green", "Dr. Mandela", "Dr. Patel"];

    const db = connect(dbName);
    const collection = db[colName];

    for (let i = 0; i < patientCount; i++) {
        let patient = {
            "name": names[Math.floor(Math.random() * names.length)],
            "age": Math.floor(Math.random() * 60) + 18,
            "ailment": ailments[Math.floor(Math.random() * ailments.length)],
            "doctor": doctors[Math.floor(Math.random() * doctors.length)],
            "admitted": Math.random() > 0.5,
            "appointments": [
                { "date": "2024-09-01", "reason": "Checkup" },
                { "date": "2024-09-15", "reason": "Follow-up" }
            ]
        };
        collection.insertOne(patient);
    }
}


/*
    * -------------------------------------------------------------------------------------------
    * Find and return all patients currently admitted
*/ 
function findAdmittedPatients(dbName, colName) {
    const db = connect(dbName);
    return db[colName].find({ "admitted": true }).toArray();
}


/*
    * -------------------------------------------------------------------------------------------
    * Update the admission status of a patient by their name
*/ 
function updatePatientAdmission(dbName, colName, patientName, status) {
    const db = connect(dbName);
    db[colName].updateOne({ "name": patientName }, { $set: { "admitted": status } });
}


/*
    * -------------------------------------------------------------------------------------------
    * Remove all patients who have been discharged (admitted = false)
*/ 
function removeDischargedPatients(dbName, colName) {
    const db = connect(dbName);
    db[colName].deleteMany({ "admitted": false });
}


/*
    * -------------------------------------------------------------------------------------------
    * doctorStats
    * List all doctors and the number of patients they are currently treating
*/ 
function doctorStats(dbName, colName) {
    const db = connect(dbName);
    return db[colName].aggregate([
        { $group: { _id: "$doctor", patientCount: { $sum: 1 } } },
        { $sort: { _id: 1 } }
    ]).toArray();
}


/*
    * -------------------------------------------------------------------------------------------
    * doctorPatientList
    * Show the list of patients under a specific doctor
*/ 
function doctorPatientList(dbName, colName, doctorName) {
    const db = connect(dbName);
    return db[colName].find({ "doctor": doctorName }).toArray();
}


/*
    * -------------------------------------------------------------------------------------------
    * activeDoctorsMR
    * Count the total number of patients for each doctor and store in 'DoctorActivity'
*/ 
function activeDoctorsMR(dbName, colName) {
    const db = connect(dbName);
    db[colName].aggregate([
        { $group: { _id: "$doctor", totalPatients: { $sum: 1 } } },
        { $out: "DoctorActivity" }
    ]);
}


/*
    * -------------------------------------------------------------------------------------------
    * appointmentStats
    * Display the total number of appointments each doctor has, grouped by doctor name
*/ 
function appointmentStats(dbName, colName) {
    const db = connect(dbName);
    return db[colName].aggregate([
        { $unwind: "$appointments" },
        { $group: { _id: "$doctor", totalAppointments: { $sum: 1 } } },
        { $out: "Appointments" }
    ]).toArray();
}
