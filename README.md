# SREscape - Jeu Interactif de Simulation d'Incident SRE
## 📦 Structure des Fichiers

```
SREscape/
├── SREscape.java                   # Version sans images
├── images/                         # Dossier contenant les images
│   ├── logo_entreprise.png         # Logo
│   ├── slide_3_0.png               # Support client
│   ├── slide_4_1.png               # Dashboard OK
│   ├── slide_6_0.png               # Dashboard KO
│   └── slide_7_0.png               # Dashboard restauré
├── run.sh / run.bat                # Scripts pour version sans images
└── README.md                       # Ce fichier
```

## 🚀 Installation et Lancement

### Prérequis
- Java JDK 8 ou supérieur
- Les fichiers doivent être dans le même dossier que le dossier `images/`

### Fichier config.properties

Le fichier `config.properties` permet de personnaliser certains paramètres du jeu sans modifier le code source.

#### 1. Durée du jeu
```properties
game.duration.minutes=10
```
- **Description** : Durée maximale du jeu en minutes
- **Type** : Nombre entier
- **Par défaut** : 10 minutes
- **Exemples** :
  - `game.duration.minutes=5` → 5 minutes
  - `game.duration.minutes=15` → 15 minutes

#### 2. Nom du premier service
```properties
service.first.name=Gestion Carriere
```
- **Description** : Nom du service principal affecté par l'incident
- **Type** : Texte libre
- **Par défaut** : "Gestion Carriere"
- **Exemples** :
  - `service.first.name=Paie`
  - `service.first.name=Facturation`
  - `service.first.name=Commandes`

#### 3. Nom du second service
```properties
service.second.name=Referentiel Individu
```
- **Description** : Nom du service dépendant en panne
- **Type** : Texte libre
- **Par défaut** : "Referentiel Individu"
- **Exemples** :
  - `service.second.name=Base Clients`
  - `service.second.name=API Authentification`
  - `service.second.name=Service Produits`

#### Windows
Double-cliquez sur `run.bat`

OU en ligne de commande :
```cmd
javac SREscape.java
java SREscape
```

#### Linux/macOS
```bash
chmod +x run.sh
./run.sh
```

OU :
```bash
javac SREscape.java
java SREscape
```

## 📊 Système de Points

### Points gagnés :
- ✅ **Bonne décision** : +5 points (passe à l'écran suivant)

### Points perdus :
- ⚠️ **Mauvaise décision** : -1 point
- ⚠️ **Mauvaise décision avec perte de temps** : -2 point

### Écran final (choix multiples) :
- 📊 **Consulter la plate-forme d'observabilité** : +5 points (meilleur choix)
- 📞 **Rappeler l'utilisateur** : +3 points (bon choix)
- ⏹️ **Ne rien faire** : +1 point (choix minimal)

## 🎯 Scénario du Jeu

### Étape 1 : Incident signalé 🚨
**Image** : Ingénieur SRE recevant l'appel d'un utilisateur en détresse

Un utilisateur signale une erreur sur l'application.
👉 **Bon choix** : Consulter la plate-forme d'observabilité

### Étape 2 : Tableau de bord 📊
**Image** : Dashboard montrant des erreurs (statut OK avec pics d'erreurs)

Le service est en erreur.
👉 **Bon choix** : Regarder les journaux (logs)

### Étape 3 : Analyse des logs 📝
Les logs montrent des timeouts avec un autre service
👉 **Bon choix** : Consulter la plate-forme d'observabilité

### Étape 4 : Identification du service défaillant 🔍
**Image** : Dashboard montrant le service en panne (statut KO en rouge)

Le statut du service est en erreur.
👉 **Bon choix** : Redémarrer le serveur d'application

### Étape 5 : Service rétabli ✅
**Image** : Dashboard montrant le retour à la normale (statut OK)

Le service est de nouveau opérationnel.

## 🎨 Visuels Inclus

Tous les visuels sont inclus, il faut juste ajouter votre logo d'entreprise : logo_entreprise.png

## 🎮 Contrôles
- **Souris** : Cliquez sur les boutons pour faire vos choix

## 🏆 Barème d'évaluation finale
- **30+ points** : ⭐ Expert SRE ! Intervention parfaite !
- **25-29 points** : ✅ Très bien ! Bonne gestion de l'incident.
- **20-24 points** : 👍 Bien ! Quelques améliorations possibles.
- **15-19 points** : ⚠️ Moyen. Revoyez les bonnes pratiques SRE.
- **< 15 points** : ❌ À améliorer. Formez-vous davantage !

## ⚠️ Dépannage

### Les images ne s'affichent pas
- Vérifiez que le dossier `images/` est dans le même répertoire que le fichier .java
- Vérifiez que tous les fichiers PNG sont présents dans le dossier images/
- Si les images manquent, le jeu fonctionnera quand même mais sans illustrations

### Erreur de compilation
- Assurez-vous d'avoir le JDK complet (pas seulement le JRE)
- Vérifiez que Java est bien installé : `java -version`

### Le jeu ne se lance pas en plein écran
- C'est normal sur certains systèmes
- Appuyez sur F11 ou maximisez manuellement la fenêtre

## 🎓 Objectifs Pédagogiques

Ce jeu enseigne les bonnes pratiques SRE :
1. **Observer avant d'agir** : Toujours consulter les métriques et logs
2. **Méthodologie d'investigation** : Partir du symptôme vers la cause
4. **Suivi post-incident** : Vérifier que tout est revenu à la normale

## 📝 Licence
Programme éducatif libre d'utilisation et de modification.
