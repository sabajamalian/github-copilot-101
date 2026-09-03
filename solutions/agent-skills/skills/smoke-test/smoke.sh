#!/usr/bin/env bash
set -uo pipefail

BASE="${1:-http://localhost:8080}"
failures=0

check() {
  local method="$1" path="$2" expected="$3"
  local code
  code=$(curl -s -o /dev/null -w '%{http_code}' -X "$method" "$BASE$path")
  if [ "$code" = "$expected" ]; then
    printf 'ok    %-6s %-16s %s\n' "$method" "$path" "$code"
  else
    printf 'FAIL  %-6s %-16s got %s, want %s\n' "$method" "$path" "$code" "$expected"
    failures=$((failures + 1))
  fi
}

echo "Smoke testing $BASE"
check GET /books       200
check GET /books/1     200
check GET /members     200
check GET /members/1   200
check GET /loans       200

# Known bugs, asserted so we notice when they get fixed.
# A missing book answers 200 with an empty body; a missing member blows up with 500.
check GET /books/999   200
check GET /members/999 500

echo
if [ "$failures" -eq 0 ]; then
  echo "All checks passed."
else
  echo "$failures check(s) failed."
fi
exit "$failures"
