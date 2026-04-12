Il faudrait mieux ranger les implémentations / classes métier / contrats ici
donc en gros grouper les fichiers dans des dossiers par rôle, ex : 

dataAccessPackage/
├── api/
│   ├── GenericDAO.java
│   ├── OrderDataAccess.java
│   ├── OrderLineDataAccess.java
│   ├── ProductDataAccess.java
│   ├── SearchDataAccess.java
│   └── TableDataAccess.java
│
├── impl/
│   ├── OrderDBAccess.java
│   ├── OrderLineDBAccess.java
│   ├── ProductDBAccess.java
│   ├── SearchDBAccess.java
│   └── TableDBAccess.java
│
├── core/
│   ├── AbstractDAO.java
│   └── DBConnection.java

ORDER LINE ID => a déplacer dans models
