Aplikasi terdiri dari dua modul yakni Server dan Client : 
Client : 
Client terdiri dari 4 tahap yakni : 
1.Memilih Algoritma
2.Konfigurasi parameter awal yang diperlukan sesuai algoritma terpilih
3.Pengaturan pelacakkan history proses(Logging) sebelum memulai proses
4.Memulai proses
5.Menampilkan Hasil Proses
Client dapat membangkitkan / membuat file random untuk disorting yang memiliki parameter / isian sebagai berikut : 
1.Jumlah Bilangan : menentukan banyak array, key atau angka yang mau diurutkan
2.Jangkauan : menentukan variasi key atau angka antara 0 - nilai isian.
3.Nama File : menentukan nama file hasil generate / pembangkitan untuk disimpan dalam Disk Drive.
Berikut petunjuk penggunaan aplikasi berdasarkan algoritma masing - masing : 
-.Algoritma Sequential MergeSort : 
Parameter yang diperlukan / dapat diisi : 
1.Input file berisi angka acak : pemilihan file - file (multi / jamak) input untuk diurutkan biasanya merupakan hasil generate.
2.Jumlah ulang / Running Test : pengulangan proses algoritma untuk mendapatkan nilai rata - rata waktu eksekusi algoritma (Average of Elapsed Time).
-.Algoritma Parallel MergeSort : 
Parameter yang diperlukan / dapat diisi : 
1.Input file berisi angka acak : pemilihan file - file (multi / jamak) input untuk diurutkan biasanya merupakan hasil generate.
2.Jumlah ulang / Running Test : pengulangan proses algoritma untuk mendapatkan nilai rata - rata waktu eksekusi algoritma (Average of Elapsed Time).
3.Core Client : Jumlah execution core / prosesor komputer yang dipakai dalam eksekusi Parallel oleh algoritma MergeSort
-.Algoritma Distributed MergeSort : 
Parameter yang diperlukan / dapat diisi : 
1.Input file berisi angka acak : pemilihan file - file (multi / jamak) input untuk diurutkan biasanya merupakan hasil generate.
2.Jumlah ulang / Running Test : pengulangan proses algoritma untuk mendapatkan nilai rata - rata waktu eksekusi algoritma (Average of Elapsed Time).
3.Core Client : Jumlah execution Core / prosesor komputer yang dipakai oleh client dalam mengerjakan hasil proses dari berbagai server.
4.ChunkSize : Jumlah maksimal array yang dikirim ke server.
5.Server terdeteksi : dengan tombol scan akan merefresh / menyegarkan informasi tentang server yang masih terhubung dengan client dalam sistem.
6.Tampilkan & set Server : menampilkan IP server - server yang terhubung dengan sistem dengan jumlah core yang terset masing - masing server, disini dapat juga mengubah jumlah core server - server tsb.


 Server : 
Server hanya dapat menjalankan proses yang diperintahkan oleh client, pada server hanya dapat melihat / membaca parameter dan proses yang diperintahkan oleh client.
Pada server hanya dapat buat log atau melakukan track log selama client belum melakukan lock atau kunci resource server (ditandai dengan tombol log terdisable).


Logging / melakukan riwayat proses : 
Tujuan logging tidak diatur sebagai pengaturan default / bawaan adalah pada sistem terdistribusi akan berefek menjadi lambat.
Logging memerlukan resource I/O yang mana akses ke disk adalah akses paling lambat, supaya log dapat di tampilkan maka harus dilakukan bersamaan dengan proses maupun sebelum dan sesudah proses, 
yang mana elapsed time / waktu eksekusi akan terhitung bersamaan dengan Logging dan melencengkan keakuratan penelitian.
 Logging memiliki tiga kriteria : 
1.No log : tidak melakukan log sama sekali.(pengaturan khusus untuk Distributed MergeSort).
2.Normal Log : melakukan logging minimum, dengan memberitahu kapan proses diterima , di jalankan dan dilepaskan (selesai).
3.Debug Log : melakukan Logging seperti normal Log tetapi dengan tambahan menampilkan array atau key - key yang diterima, baik sebelum proses maupun sesudah proses.(dapat menyebabkan clutter).
NB : Logging hanya mempengaruhi waktu eksekusi algoritma distributed MergeSort. Jadi saat penelitian disarankan untuk tidak melakukan LOG.
Checkbox "Auto Save Log" : akan menyimpan log yang tertulis pada textArea saat sesi berlangsung kedalam folder : 
apabila untuk client : folder "Log Client (autosave)"
apabila untuk server : folder "Log Server (autosave)".


Hasil proses : 
1.buka stat : Menampilkan waktu - waktu eksekusi (berdasarkan jumlah loop running test) perFile berdasarkan algoritma yang digunakan.
2.buka hasil sort : memilih file hasil sortir proses pengurutan, format file adalah :  
untuk sequential merge sort : [nama input file] [size],
 untuk parallel merge sort : [nama input file] [size] [core],
 untuk distributed merge sort : [nama input file] [size] [host].
NB : dalam file, apabila terdapat nama yang sama maka akan diberi "(counter)" pada akhir nama file, nilai counter tertinggi adalah file paling update.


Folder - folder yang akan dicreate Oleh aplikasi : 
1.outputfile : terdiri dari tiga sub folder masing - masing sesuai algoritma yang terpilih (sequential merge sort, parallel merge sort dan distributed merge sort),
didalam sub folder akan berisi file hasil pengurutan masing - masing algoritma.
2.Generated Input File : merupakan default folder yang dibuka ketika memilih file input dan juga merupakan folder tempat hasil generate / membangkitkan file bilangan acak disimpan.
3.Statistik Log : merupakan folder tempat menyimpan data statistik waktu eksekusi hasil proses berdasarkan algoritma yang terpilih.
4.Log Client (autosave) : folder tempat menyimpan file hasil capture logging pada text area oleh program client.
5.Log Server (autosave) : folder tempat menyimpan file hasil capture logging pada text area oleh program server.


Hidden Folder : 
1.UserSavedClientLog : folder tempat menyimpan file hasil capture bagian sesi text area di program client manual oleh user.
1.UserSavedServerLog : folder tempat menyimpan file hasil capture bagian sesi text area di program Server manual oleh user.


Hidden Utility (pengaturan yang digunakan saat testing oleh programmer, tidak diperutukkan oleh user!!!) : 
main: 
double klik status label
double klik logo USU

server:
double klik text field client terdeteksi
double klik text field core terpakai
double klik text field hostname RMI
double klik label "Buat Log"

client
double klik status core
double klik status running test
double klik status chunksize
double klik label hasil proses