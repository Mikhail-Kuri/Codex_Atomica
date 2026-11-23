# Next steps

> **Usage** : à lire en début de session (après `docs/PROJECT.md`), à mettre à jour en fin de session : cocher ou retirer ce qui est fait, ajouter ce qui a été découvert.
> **Dernière mise à jour** : 2026-11-22 · `main` = `31d50f9` (poussé) · 73 tests verts.

## 1. Prochaine session (recommandé, ~1 session)

Petits correctifs révélés en jouant. Une branche `fix/…` ou `feature/…` par sujet.

- [ ] **Brûlure et Saignement qui grandissent avec le niveau.** Aujourd'hui fixes (4 et 5 dégâts) : insignifiants contre 380 PV au niveau 10.
  *Décision à prendre* : dégâts en % des PV max de la cible, ou selon le niveau / une stat du lanceur ?
  *Fini quand* : à niveau égal, un effet pèse à peu près autant au niveau 1 qu'au niveau 40 (test sur les deux niveaux).
- [ ] **Frappe sacrificielle à revoir** (`SelfAttack`) : n'utilise que la Force, fait 4 à 6 dégâts.
  *Décision à prendre* : son rôle (gros coup contre un coût en PV ?) et son ratio dégâts / auto-dégâts.
  *Fini quand* : elle bat l'Attaque de base en dégâts, au prix de PV, et le journal dit « hurts itself » au lieu de « takes X damage from Electric Mutant ».
- [ ] **`DuelTest.enemyWinsWhenPlayerFalls`** pile à sa limite de 5 tours : lui donner de la marge (ex. 20 tours) pour qu'un futur réglage ne le casse pas.

## 2. Ensuite, par ordre conseillé

1. **Compétences en données** (`skills.json`, briques d'effet de `docs/data-format.md`), avant d'écrire de nouvelles compétences. Puis effets de statut et états mentaux en données.
2. **Défense selon l'état mental** (`onDefend` par personnalité) et **bonus de style** (XP de combat +25 % max). Le crochet existe : `AbstractMentalState.deltaFor`.
3. **Équipement** : 8 emplacements, niveau d'objet, niveau requis, objets uniques. Écrire d'abord son document de design (`design/gdd/`).
4. **Codex des ennemis** : compétences inconnues jusqu'à être vues, intentions affichées seulement si connues, cibles visibles. Document de design d'abord.
5. **Quêtes et missions** : source principale d'XP (formules X5, X7 de la progression).
6. **Clans** : petit bonus de stats ou passif.
7. **Combats en équipe** (cibles, protéger un allié, XP de groupe).

## 3. Décisions attendues du propriétaire

Ne pas trancher à sa place : proposer des options, attendre son choix.

| Sujet | Question | Où |
|---|---|---|
| XP au niveau 60 | Perdue ou convertie (talents, monnaie, réputation) ? | `design/gdd/progression.md` Q1 |
| Plafond par acte | Souple (recommandé) ou strict ? | progression Q2 |
| Acte 4 | Quelle progression remplace les niveaux ? | progression Q3 |
| Résonance | Reste un multiplicateur d'arme, ou devient une stat d'équipement ? | progression Q5 |
| Bonus de style | Critère exact par personnalité | progression Q6 |
| XP de groupe | Partagée ou entière pour chacun ? | progression Q7 |
| Héros / ennemis | Paladin, Éclaireur, Mutant = héros ; Gnoll, Rat = ennemis ? | `docs/data-format.md` Q1 |
| Santé mentale | `CharacterState.currentSanity` inutilisé : stat ou à retirer ? | data-format Q2 |
| Coût des compétences | Mana, temps de recharge, ressource de clan ? | data-format Q3 |

## 4. Ménage

- [ ] Branches locales déjà fusionnées : `feature/combat-log`, `feature/combat-menu`, `feature/data-format`, `feature/levels`, `feature/progression`, `fix/duel-test-determinism`, `claude/codex-atomica-java-9bd13c` (à supprimer avec l'accord du propriétaire).
- [ ] Branche distante `origin/fix/defensive_changes` non fusionnée (un TODO dans `DefensiveAction` + `GameTesterDefense_2`) : fusionner ou fermer ?
- [ ] `src/main/java/org/example/important_directory_TODOS/TODO_List.txt` (« base de données pour la logique des classes ») : couvert par les fichiers JSON ; à retirer ou à garder ?
- [ ] `src/main/java/org/example/inventory/` ne contient qu'un fichier texte vide.
- [ ] `GameTester` : scénarios manuels en français, dont certains planifient plusieurs actions par personnage (refusées depuis le budget d'actions). À mettre à jour ou à remplacer par des tests.
- [ ] Preuves visuelles : les standards demandent des captures dans `production/qa/evidence/` pour les changements visibles ; aucune n'a été gardée jusqu'ici.
