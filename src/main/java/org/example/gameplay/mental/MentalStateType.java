package org.example.gameplay.mental;

public enum MentalStateType {
    FAITH {
        public MentalState create() { return new FaithMentalState(); }
    },
    RAGE {
        public MentalState create() { return new RageMentalState(); }
    },
    CALM{
        public MentalState create() { return new CalmMentalState(); }
    },
    HONOUR{
        public MentalState create() { return new HonourMentalState();}
        
    },
    CORRUPTION{
        public MentalState create() { return new CorruptionMentalState();}
    };

    public abstract MentalState create();


}
