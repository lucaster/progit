# Flusso di lavoro

Guida pratica per contribuire a questo repository e pubblicare una release.
Tutti i comandi vanno eseguiti dalla radice del progetto, con `gh` autenticato e
`main` protetta: **nessun commit diretto su `main`**, solo pull request.

## Prerequisiti

- Java 17+ e Maven 3.9+
- `git` e [GitHub CLI (`gh`)](https://cli.github.com/) installati e autenticati:
  ```bash
  gh auth status
  ```
- Configurazione locale già pronta in questo repo (`user.name`, `user.email`).

## Flusso di una modifica (pull request)

### 1. Allinea `main` e crea un branch

```bash
git checkout main
git pull
git checkout -b feat/nome-breve
```

Convenzione per il nome del branch:

| Prefisso | Uso |
|---|---|
| `feat/` | nuova funzionalità |
| `fix/` | correzione di un bug |
| `docs/` | documentazione |
| `chore/` | manutenzione, dipendenze, CI |

### 2. Fai le modifiche e verifica in locale

```bash
mvn verify
```

Il comando compila ed esegue i test: deve passare **prima** di aprire la PR.

### 3. Commit

```bash
git add <file>
git commit -m "feat: descrizione breve"
```

Il **titolo** del commit (e poi della PR) diventa una voce del changelog della
release, quindi scrivilo pensando a chi legge:

```
feat: aggiungi il saluto configurabile
fix: gestisci il saluto vuoto
docs: aggiorna il README
chore: aggiorna le GitHub Actions
test: aggiungi il test del saluto
```

### 4. Push e apertura della PR

```bash
git push -u origin feat/nome-breve
gh pr create --fill          # usa titolo/descrizione dai commit
```

Per compilare il template manualmente (consigliato la prima volta):

```bash
gh pr create --base main --title "feat: ..." --body "..."
```

Da web: GitHub mostra l'invito *"Compare & pull request"* subito dopo il push.

### 5. Attendi la CI e mergia

```bash
gh pr checks --watch          # attende che il check "build" diventi verde
gh pr merge --squash --delete-branch
```

- La **CI** (`.github/workflows/ci.yml`) esegue `mvn verify` su ogni PR.
- La **branch protection** impedisce il merge finché il check `build` non è verde.
- Il **squash** porta una sola voce pulita nella storia di `main` e nel changelog;
  `--delete-branch` cancella il branch remoto dopo il merge.

### 6. Sincronizza in locale

```bash
git checkout main
git pull
```

## Flusso di una issue (con la PR che la chiude)

Quando una modifica nasce da una issue, questo è il giro completo: dalla
segnalazione alla chiusura automatica al merge.

### 1. Crea la issue

```bash
gh issue create --title "Il saluto con un nome vuoto stampa 'Hello, !'" \
  --body "Passi per riprodurre..." --label bug
```

- Corpo lungo: mettilo in un file e usa `--body-file issue.md`.
- Aggiungi `--assignee @me` per assegnartela subito.

L'output è l'URL della issue; **annotane il numero** (es. `6`).

Da web: **Issues → New issue**, scegliendo il template.

### 2. Crea il branch e lavora

```bash
git checkout main && git pull
git checkout -b fix/blank-name-argument
# ... modifichi e testi ...
mvn verify
git add src
git commit -m "fix: ignore a blank name argument"
git push -u origin fix/blank-name-argument
```

### 3. Apri la PR collegandola alla issue

Il collegamento si fa con una **parola chiave + numero** nel corpo della PR:

```bash
gh pr create --base main --head fix/blank-name-argument \
  --title "fix: ignore a blank name argument" \
  --body "Gestisce l'argomento vuoto.

Closes #6"
```

Parole chiave riconosciute (chiudono la issue al merge): `Closes #6`,
`Fixes #6`, `Resolves #6`; valgono anche al plurale (`Closes #6, #7`) e in
qualsiasi punto del corpo.

Da web: dopo il push GitHub propone *Compare & pull request*; scrivi `Closes #6`
nella descrizione.

### 4. Verifica il collegamento e la CI

```bash
gh pr view --json closingIssuesReferences --jq '.closingIssuesReferences[].number'   # -> 6
gh pr checks --watch
```

### 5. Mergia: la issue si chiude da sola

```bash
gh pr merge --squash --delete-branch
```

Al merge GitHub:

- chiude automaticamente la issue (#6) come *"closed this as completed"*;
- rende cliccabile il riferimento `#6` nella PR e nel changelog della release.

### Riepilogo

```bash
gh issue create --title "..." --body "..." --label bug       # 1. issue (numero N)
git checkout main && git pull
git checkout -b fix/nome-breve                                # 2. branch
# ... modifichi e testi ...
mvn verify
git commit -am "fix: ..." && git push -u origin fix/nome-breve  # 3. commit + push
gh pr create --fill --body "Closes #N"                        # 4. PR collegata
gh pr checks --watch                                          # 5. CI verde
gh pr merge --squash --delete-branch                          # 6. merge -> issue chiusa
```

## Flusso di una release

Il changelog viene **generato** dai titoli delle PR mergeate dall'ultima release.
La pubblicazione è automatizzata dal workflow `.github/workflows/release.yml`:
al push di un tag `v*` compila il progetto e crea la release con il jar allegato.

### 1. Allinea `main`

```bash
git checkout main
git pull
```

### 2. Aggiorna la versione nel `pom.xml`

```xml
<version>0.3.0</version>
```

Fallo con una PR normale (`chore: release 0.3.0`) e mergiala. La versione del
`pom.xml` e il tag della release devono coincidere: il jar allegato si chiama
`progit-<versione>.jar`.

### 3. Crea e spingi il tag

Il tag usa il prefisso `v`:

```bash
git tag -a v0.3.0 -m "progit v0.3.0"
git push origin v0.3.0
```

### 4. La release si crea da sola

Il push del tag fa partire il workflow `.github/workflows/release.yml`, che:

1. compila con `mvn -B clean package` (test inclusi);
2. crea la release con il changelog generato (`--generate-notes`);
3. allega il jar eseguibile `target/progit-<versione>.jar` come asset.

Segui l'avanzamento con:

```bash
gh run watch
```

Se preferisci pubblicare a mano (senza il workflow), compila e allega il jar:

```bash
mvn -B package
gh release create v0.3.0 --title "progit v0.3.0" --generate-notes target/progit-0.3.0.jar
```

Varianti utili di `gh release create`:

- `--draft` crea una bozza da rivedere prima di pubblicare
  (`gh release edit v0.3.0 --draft=false` per pubblicarla);
- `--prerelease` per una release candidata (`v0.3.0-rc.1`);
- `--notes-file NOTE.md` per sostituire le note generate con un testo tuo.

Da web: **Releases → Draft a new release**, scegli il tag, premi
*Generate release notes* e trascina il jar tra gli asset.

### Risultato

GitHub compila automaticamente:

```
## What's Changed
* feat: ... by @lucaster in #N
## New Contributors
**Full Changelog**: .../compare/v0.2.0...v0.3.0
```

Alla release è allegato il jar compilato (`progit-<versione>.jar`) nella sezione
**Assets**.

## Riferimenti rapidi

| Operazione | Comando |
|---|---|
| Vedere le issue aperte | `gh issue list` |
| Vedere una issue | `gh issue view 6` |
| Vedere le PR aperte | `gh pr list` |
| Vedere i check di una PR | `gh pr checks --watch` |
| Vedere le release | `gh release list` |
| Vedere una release | `gh release view v0.2.0` |
| Stato della CI più recente | `gh run list` |
