package backendAdministradorCompetenciasFutbolisticas.Dtos;

public class Posicion {
    String club;
    Integer PTS;
    Integer PJ;
    Integer V;
    Integer E;
    Integer D;
    Integer GF;
    Integer GC;
    Integer DIF;

    public Posicion(String club){
        this.club = club;
        this.PTS = 0;
        this.PJ = 0;
        this.V = 0;
        this.E = 0;
        this.D = 0;
        this.GF = 0;
        this.GC = 0;
        this.DIF = 0;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public Integer getPTS() {
        return PTS;
    }

    public void setPTS(Integer PTS) {
        this.PTS = PTS;
    }

    public Integer getPJ() {
        return PJ;
    }

    public void setPJ(Integer PJ) {
        this.PJ = PJ;
    }

    public Integer getV() {
        return V;
    }

    public void setV(Integer v) {
        V = v;
    }

    public Integer getE() {
        return E;
    }

    public void setE(Integer e) {
        E = e;
    }

    public Integer getD() {
        return D;
    }

    public void setD(Integer d) {
        D = d;
    }

    public Integer getGF() {
        return GF;
    }

    public void setGF(Integer GF) {
        this.GF = GF;
    }

    public Integer getGC() {
        return GC;
    }

    public void setGC(Integer GC) {
        this.GC = GC;
    }

    public Integer getDIF() {
        return DIF;
    }

    public void setDIF(Integer DIF) {
        this.DIF = DIF;
    }

    public void sumarPuntosVictoria(){
        this.PJ += 1;
        this.PTS += 3;
        this.V += 1;
    }
    public void sumarPuntosDerrota(){
        this.PJ += 1;
        this.PTS += 0;
        this.D += 1;
    }
    public void sumarPuntosEmpate(){
        this.PJ += 1;
        this.PTS += 1;
        this.E += 1;
    }

    public void sumarGolesAFavor(Integer golesAFavor){
        this.GF += golesAFavor;
    }
    public  void sumarGolesEnContra(Integer golesEnContra){
        this.GC += golesEnContra;
    }

    public void actualizarDiferencia(){
        this.DIF = this.GF - this.GC;
    }

    @Override
    public String toString() {
        return "Posicion{" +
                "club='" + club + '\'' +
                ", PTS=" + PTS +
                ", PJ=" + PJ +
                ", V=" + V +
                ", E=" + E +
                ", D=" + D +
                ", GF=" + GF +
                ", GC=" + GC +
                ", DIF=" + DIF +
                '}';
    }
}
