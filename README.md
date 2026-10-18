# Ice Slabs & Boat Physics

A small Fabric mod that adds slabs of **ice**, **packed ice** and **blue ice**.

- The slabs are exactly as slippery as the full blocks, for players, mobs and dropped items.
- Boats can climb up onto ice slabs, so you can build ice-boat tracks with half-block ramps.
- Crafting: 3 blocks in a row give 6 slabs. Slabs drop themselves when mined.
- Ice slabs are translucent, like vanilla ice.

## Versions

| Branch | Minecraft | Java | Status |
|---|---|---|---|
| `main` | 26.3 | 25 | actively developed |
| `1.21.11` | 1.21.11 | 21 | maintained |

Downloads are on the [Releases](https://github.com/dmitrymdd/iceslabs-n-boatphysics/releases) page.
Requires [Fabric Loader](https://fabricmc.net/use/) and [Fabric API](https://modrinth.com/mod/fabric-api).

## Building

```
./gradlew build        # Windows: gradlew.bat build
```

Needs JDK 25 to run Gradle (the 1.21.11 branch still compiles for Java 21).
The jar ends up in `build/libs/` (ignore the `-sources.jar`).

## Releasing

Pushing a tag like `v1.1.0+26.3` makes GitHub Actions build the mod and publish a release with the jar.

## License

[CC0 1.0](LICENSE)
