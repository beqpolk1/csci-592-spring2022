* Track
    * Structural Variation 1
        * Attribute genres: change to PTuple of String
    * Structural Variation 2
        * Attribute genres: change to PTuple of String
* Artist
    * Delete Structural Variation 1 - had lyricsTracks reference to Tracks entities
    * Delete reference composedTracks from Structural Variation 2
    * Set Variation ID of Variation 2 to 1
* Album
    * Delete Structural Varations 2 & 3 - had prizes aggregate of Prize entities
    * Structural Variation 1
        * Attribute formats: change to PTuple of String
        * Delete attribute availability
        * Attribute genres: change to PTuple of String
        * Add Aggregate tracks: set to aggregate AlbumTrack entity type
        * Delete Reference tracks
    * Structural Variation 4
        * Attribute formats: change to PTuple of String
        * Delete attribute availability
        * Change cardinality of Aggregate reviews to have upper bound of -1 (i.e. unbounded)
        * Add Aggregate tracks: set to aggregate AlbumTrack entity type
        * Delete Reference tracks
        * Set Variation ID to 2
    * Structural Variation 5
        * Attribute formats: change to PTuple of String
        * Delete attribute availability
        * Add Aggregate tracks: set to aggregate AlbumTrack entity type
        * Delete Reference tracks
        * Set Variation ID to 3
* Prize
    * Delete this entity
* Review
    * Delete Structural Variation 1 - had media attribute of String
    * Set Variation ID of Variation 2 to 1
* Create Entity type AlbumTrack
    * One structural variation - Structural Variation 1
    * Attribute track_number - Primitive Type Number
    * Reference track_id
        * Original Type - String
        * Refs to - Entity Type Track
        * Upper bound - 1
