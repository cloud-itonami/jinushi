# jinushi (地主)

World land-ownership acquisition and reconciliation actor. The implementation is actor-owned:
all runtime namespaces are `jinushi.*`, and `manifest.edn` declares the actor's dataset contract.

## Layout

- `manifest.edn`, `kotoba.app.edn`, and `data/` are canonical EDN.
- `src/` and `test/` contain the Clojure/CLJC implementation and tests.
- External raw/derived acquisition artifacts remain in the separately managed
  `80-data/jinushi-land` dataset; they are not duplicated in this implementation repository.
- JSON interoperability artifacts, if introduced, belong under `wire/`.
- Go/TinyGo, Python, and shell implementations are deprecated and must not be added.

Run `clojure -M -m jinushi.test-runner` and `bb scripts/audit.clj`.
