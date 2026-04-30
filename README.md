# Online Reservation System

This project is a simple Java Swing based Online Reservation System created for internship work in Java development.

## Modules

- Login Form
- Reservation System
- Cancellation Form

## Features

- User login with stored credentials
- Reservation entry with automatic train name lookup
- Automatic PNR generation
- Cancellation using PNR search
- Local persistent storage using files inside the `data` folder

## Default Login

- `admin / admin123`
- `staff / staff123`

## Project Structure

```text
src/com/reservation/
  Main.java
  model/Reservation.java
  storage/DataStore.java
  ui/LoginFrame.java
  ui/MainFrame.java
  ui/ReservationPanel.java
  ui/CancellationPanel.java
```

## How to Run

Compile:

```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
```

Run:

```powershell
java -cp out com.reservation.Main
```

## Storage

- `data/users.properties` stores login ids and passwords
- `data/reservations.csv` stores reservations and cancellation status

This keeps the project easy to run without adding external database drivers. If your internship requires MySQL later, this structure can be upgraded by replacing `DataStore` with JDBC code.
