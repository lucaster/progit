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

Per collegare una issue alla PR, cita `Closes #12` nella descrizione: al merge
GitHub chiude la issue automaticamente.

## Flusso di una release

La release è una funzione nativa di GitHub: non c'è nessun workflow custom.
Il changelog viene **generato** dai titoli delle PR mergeate dall'ultima release.

### 1. Allinea `main`

```bash
git checkout main
git pull
```

### 2. Aggiorna la versione nel `pom.xml`

```xml
<version>0.2.0</version>
```

Fallo con una PR normale (`chore: release 0.2.0`) e mergiala. La versione del
`pom.xml` e il tag della release devono coincidere.

### 3. Crea e spingi il tag

Il tag usa il prefisso `v`:

```bash
git tag -a v0.2.0 -m "progit v0.2.0"
git push origin v0.2.0
```

### 4. Crea la release con il changelog generato

```bash
gh release create v0.2.0 --title "progit v0.2.0" --generate-notes
```

Varianti utili:

- `--draft` crea una bozza da rivedere prima di pubblicare
  (`gh release edit v0.2.0 --draft=false` per pubblicarla);
- `--prerelease` per una release candidata (`v0.2.0-rc.1`);
- `--notes-file NOTE.md` per sostituire le note generate con un testo tuo;
- aggiungi file come asset in coda al comando, es. `... -- target/progit-0.2.0.jar`.

Da web: **Releases → Draft a new release**, scegli il tag e premi
*Generate release notes*.

### Risultato

GitHub compila automaticamente:

```
## What's Changed
* feat: ... by @lucaster in #N
## New Contributors
**Full Changelog**: .../commits/v0.2.0
```

## Riferimenti rapidi

| Operazione | Comando |
|---|---|
| Vedere le PR aperte | `gh pr list` |
| Vedere i check di una PR | `gh pr checks --watch` |
| Vedere le release | `gh release list` |
| Vedere una release | `gh release view v0.2.0` |
| Stato della CI più recente | `gh run list` |
