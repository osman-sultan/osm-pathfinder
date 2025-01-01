# Route Planning on OpenStreetMap for Cyclists

A route planning application built on OpenStreetMap data to help cyclists find efficient and safe routes. The application uses heuristic-based search algorithms, including A* search, to compute optimal paths.

## Features
- **Pathfinding Algorithms**:
    - A* Search
    - Uniform Cost Search
    - Greedy Best-First Search
    - Breadth-First Search
    - Depth-First Search
- **Custom Heuristics**: Tailored A* heuristic to improve route planning performance.
- **Interactive Interface**: Pan, zoom, and set waypoints to configure routes.
- **Customizable Maps**: Supports loading any OpenStreetMap (OSM) files for route planning.
- **Cycling Safety Data Integration**: Optimize routes for cyclist safety.

## How to Run
1. Run the application by executing `main.Demo.java`.
2. **Controls**:
    - **Pan**: Click and drag with the left mouse button.
    - **Zoom**: Use the mouse scroll wheel.
    - **Set Start/Destination**: Right-click to set or clear waypoints.

## Configuration Options
- The default map (`toronto.osm`) can be replaced with any `.osm` file:
    1. Download the desired map from [OpenStreetMap Export](https://www.openstreetmap.org/export).
    2. Place the `.osm` file in the `/data` directory.
    3. Update the `osmFile` variable in the `main` method with the new map file.

## Data Sources
- **OpenStreetMap Data**: [OpenStreetMap Export](https://www.openstreetmap.org/export)
- **Cycling Safety Data**: [Toronto Police Traffic Data](https://data.torontopolice.on.ca/pages/traffic)

