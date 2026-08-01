# UI Contracts: People Directory Load

This directory is intended for contracts and specifications of external dependencies.
Since this is an E2E Selenium UI testing project, the primary interface contract is the DOM selector structure:

- Main page title must contain tag matching selector `.titulo-page h2` and value `"Directorio"`.
- Person item container must match class `.profesor.row` inside `.views-row`.
- Person name must match `.nombre a` inside the item container.
