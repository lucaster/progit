# Contributing

Grazie per l'interesse. Questo è un repo di esempio, ma segue le regole di un progetto reale.

## Flusso di lavoro

1. Crea un branch descrittivo partendo da `main`:
   - `feat/nome-breve` per nuove funzionalità
   - `fix/nome-breve` per correzioni
   - `docs/nome-breve` per la documentazione
2. Fai commit piccoli e con messaggi chiari.
3. Apri una pull request verso `main` e compila il template.
4. Attendi che la CI sia verde (`mvn verify`) e che la PR sia approvata.
5. Fai il merge (squash) e cancella il branch.

## Convenzione per i titoli

I titoli di commit e PR diventano il changelog delle release, quindi scrivili bene:

```
feat: aggiungi il saluto configurabile
fix: gestisci il saluto vuoto
docs: aggiorna il README
```

Usa `feat:`, `fix:`, `docs:`, `chore:`, `test:` come prefissi.

## Verifiche locali

Prima di aprire la PR assicurati che:

```bash
mvn verify
```

passi senza errori.
