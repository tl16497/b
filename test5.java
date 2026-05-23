import java.util.*;

abstract class LopCha {
    protected String thuocTinhChung1; 
    protected String thuocTinhChung2; 
    
    public LopCha(String thuocTinhChung1, String thuocTinhChung2) {
        this.thuocTinhChung1 = thuocTinhChung1;
        this.thuocTinhChung2 = thuocTinhChung2;
    }
    
    public abstract double tinhToanKetQua();
}

class LopCon extends LopCha {
    private double thuocTinhRieng1; 
    
    public LopCon(String thuocTinhChung1, String thuocTinhChung2, double thuocTinhRieng1) {
        super(thuocTinhChung1, thuocTinhChung2);
        this.thuocTinhRieng1 = thuocTinhRieng1;
    }

    @Override
    public double tinhToanKetQua() {
        // return thuocTinhRieng1 + 500000;                        // MẪU: Cộng thêm phụ phí cố định
        // return thuocTinhRieng1 * 30;                            // MẪU: Nhân đơn giá với số ngày
        // return thuocTinhRieng1 * 3 * 200000;                    // MẪU: Tính tiền phòng Suite
        // return thuocTinhRieng1 * Math.pow(1 + 0.06, 5);         // MẪU: Tính lãi kép ngân hàng (balance * (1+rate)^years)
        
        return thuocTinhRieng1; // Lắp công thức mặc định
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int soLuong = Integer.parseInt(sc.nextLine().trim());
        List<LopCha> danhSach = new ArrayList<>();
        
        for (int i = 0; i < soLuong; i++) {
            danhSach.add(new LopCon(sc.next(), sc.next(), sc.nextDouble()));
        }
        
        for (LopCha dt : danhSach) {
            System.out.printf("%s %.2f\n", dt.thuocTinhChung1, dt.tinhToanKetQua());
        }
    }
}

-----
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int soLuong = sc.nextInt();
        // HashMap<String, Integer> bangThongKe = new HashMap<>();       // MẪU: Không cần sắp xếp, tra cứu nhanh nhất
        // TreeMap<Character, Integer> bangThongKe = new TreeMap<>();    // MẪU: Đếm từng chữ cái A-Z (Câu 22)
        TreeMap<String, Integer> bangThongKe = new TreeMap<>();          // MẪU: Tự động sắp xếp Key là chuỗi A-Z, 0-9
        
        for (int i = 0; i < soLuong; i++) {
            String tuKhoa = sc.next();
            // bangThongKe.put(tuKhoa, bangThongKe.getOrDefault(tuKhoa, 0) + sc.nextInt()); // MẪU: Cộng dồn điểm/giá trị từ input
            // char c = tuKhoa.charAt(0); bangThongKe.put(c, bangThongKe.getOrDefault(c, 0) + 1); // MẪU: Đếm ký tự đầu tiên
            
            bangThongKe.put(tuKhoa, bangThongKe.getOrDefault(tuKhoa, 0) + 1); // MẪU: Đếm số lần xuất hiện của chuỗi
        }
        
        for (Map.Entry<String, Integer> phanTu : bangThongKe.entrySet()) {
            System.out.println(phanTu.getKey() + " " + phanTu.getValue());
        }
    }
}

-----
import java.util.*;

class LuongXuLy extends Thread {
    private int[] mangDuLieu;
    private int viTriBatDau, viTriKetThuc;
    
    // Biến kết quả cục bộ (Có thể đổi thành int, double tùy đề)
    public long ketQuaThanhPhan = 0;      

    public LuongXuLy(int[] mangDuLieu, int viTriBatDau, int viTriKetThuc) {
        this.mangDuLieu = mangDuLieu; 
        this.viTriBatDau = viTriBatDau; 
        this.viTriKetThuc = viTriKetThuc;
    }

    @Override
    public void run() {
        for (int i = viTriBatDau; i < viTriKetThuc; i++) {
            // ==========================================
            // CÁC CÂU LỆNH MẪU XỬ LÝ MẢNG TRONG LUỒNG:
            // ==========================================
            // if (mangDuLieu[i] % 2 == 0) ketQuaThanhPhan += mangDuLieu[i];      // MẪU: Chỉ cộng các số chẵn
            // ketQuaThanhPhan += (long) mangDuLieu[i] * mangDuLieu[i];           // MẪU: Tính tổng bình phương
            // ketQuaThanhPhan = Math.max(ketQuaThanhPhan, mangDuLieu[i]);        // MẪU: Tìm số lớn nhất trong đoạn
            
            ketQuaThanhPhan += mangDuLieu[i]; // MẪU MẶC ĐỊNH: Tính tổng bình thường
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int soPhanTu = sc.nextInt();
        int[] mangDuLieu = new int[soPhanTu];
        for (int i = 0; i < soPhanTu; i++) {
            mangDuLieu[i] = sc.nextInt();
        }
        
        int soLuongThread = sc.nextInt(); 
        
        int kichThuocChia = mangDuLieu.length / soLuongThread;
        LuongXuLy[] danhSachLuong = new LuongXuLy[soLuongThread];
        
        for (int i = 0; i < soLuongThread; i++) {
            int batDau = i * kichThuocChia;
            int ketThuc = (i == soLuongThread - 1) ? mangDuLieu.length : (i + 1) * kichThuocChia;
            
            danhSachLuong[i] = new LuongXuLy(mangDuLieu, batDau, ketThuc);
            danhSachLuong[i].start();
        }

        long ketQuaTongCacLuong = 0; 

        for (int i = 0; i < soLuongThread; i++) {
            danhSachLuong[i].join();
            // ketQuaTongCacLuong = Math.max(ketQuaTongCacLuong, danhSachLuong[i].ketQuaThanhPhan); // MẪU: Tìm Max toàn cục
            
            ketQuaTongCacLuong += danhSachLuong[i].ketQuaThanhPhan; // MẪU MẶC ĐỊNH: Cộng dồn tổng toàn cục
        }
        
        // System.out.printf("%.4f\n", (double) ketQuaTongCacLuong / soPhanTu); // MẪU: In số thực 4 chữ số thập phân (như bài Phương sai)
        
        System.out.println(ketQuaTongCacLuong); // MẪU MẶC ĐỊNH: In số nguyên bình thường
    }
}

-----
import java.io.*;

public class Main {
    public static void main(String[] args) {
        String tenTapTin = "data.bin";
        int giaTriNhoNhat = Integer.MAX_VALUE;
        int giaTriLonNhat = Integer.MIN_VALUE;
        long tongGiaTri = 0; 
        try (DataInputStream luongDoc = new DataInputStream(new FileInputStream(tenTapTin))) {
            while (true) {
                try {
                    int giaTriHienTai = luongDoc.readInt();
                    // tongGiaTri += giaTriHienTai;                                    // MẪU: Cộng dồn tổng
                    // if (giaTriHienTai % 2 != 0) tongGiaTri++;                       // MẪU: Đếm số lượng số lẻ
                    // giaTriNhoNhat = Math.min(giaTriNhoNhat, giaTriHienTai);         // MẪU: Tìm Min
                    // giaTriLonNhat = Math.max(giaTriLonNhat, giaTriHienTai);         // MẪU: Tìm Max
                    
                } catch (EOFException e) {
                    break; 
                }
            }
        } catch (IOException e) { }
    }
}


-----
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<DoiTuong> danhSach = new ArrayList<>();
        
        List<String> ketQua = danhSach.stream()
            // .filter(dt -> !dt.ten.isEmpty())                                // MẪU: Loại bỏ chuỗi rỗng
            // .filter(dt -> dt.giaTri >= 5.0 && dt.giaTri <= 10.0)            // MẪU: Lọc điểm hợp lệ
            .filter(dt -> dt.giaTri > 0)                                       // MẪU: Lọc giá trị dương
            
            // .sorted(Comparator.comparing(DoiTuong::getTen))                 // MẪU: Sắp xếp tăng dần theo Tên
            // .sorted(Comparator.reverseOrder())                              // MẪU: Lật ngược mảng chuỗi đơn giản
            .sorted(Comparator.comparing(DoiTuong::getGiaTri).reversed())      // MẪU: Sắp xếp giảm dần theo Giá trị
            
            // .map(String::toUpperCase)                                       // MẪU: Chuyển toàn bộ chuỗi thành in hoa
            // .collect(Collectors.joining(","))                               // MẪU: Nối các chuỗi lại bằng dấu phẩy
            .map(dt -> String.format("%s|%.2f", dt.ten, dt.giaTri))            // MẪU: Format chuỗi theo chuẩn đầu ra
            .collect(Collectors.toList());
            
        ketQua.forEach(System.out::println);
    }
}