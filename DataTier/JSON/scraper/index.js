const mysql = require("mysql8");
const fs = require("fs");

const dbConfig = {
  host: process.env.MYSQL_HOST,
  user: process.env.MYSQL_USER,
  password: process.env.MYSQL_PASSWORD,
  database: process.env.MYSQL_DATABASE,
  port: 3306
};
const pool = mysql.createPool(dbConfig);

const query = (sql, params = []) => {
  let p = new Promise((resolve, reject) => {
    pool.query(sql, params, (err, results) => {
      if (err) reject(err);
      resolve(results);
    });
  });
  return p;
};

const tableToFile = table => {
  let sql = `SELECT * FROM ${table}`;
  query(sql).catch(e => console.log(e)).then(result => {
    if (!result) {
      console.log("No results?");
      console.log(result);
      return;
    }
    let len = result.length;
    result = JSON.stringify(result);
    let filename = `./jsondata/${table.substr(table.indexOf("_")+1)}.json`;
  
    fs.writeFileSync(filename, result);
    console.log(`Wrote ${len} lines to ${filename}`);  
  });
}

let tables = [
  "dataexisting_ababanking",
  "dataexisting_areacode",
  "dataexisting_companies",
  "dataexisting_namefirst",
  "dataexisting_namelast",
  "dataexisting_upccodes",
  "dataexisting_zipcodeus",
  "datagenerated_addresses",
  "datagenerated_bankaccount",
  "datagenerated_creditcard",
  "datagenerated_dateofbirth",
  "datagenerated_driverslicenses",
  "datagenerated_phonenumber",
  "datagenerated_socialsecuritynumber"
]

for (let i = 0; i < tables.length; i++) {
  tableToFile(tables[i]);
}