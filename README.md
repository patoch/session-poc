
# Session management with Cassandra.

PoC for a json session manager based on Casssandra 2.0.
Changed to sessions are written by increment as a time serie.
On read, session changes are merged to build the latest state.
Session increments can be compacted periodically.

