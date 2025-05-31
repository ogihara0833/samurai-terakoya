package com.example.samuraitravel.service;
// 予約情報を管理するサービスクラス

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReservationRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    
    // コンストラクタ（クラスを作るときに最初に実行されるメソッド）
    // 予約情報を管理するためのリポジトリ（データベースを扱うための部品）をセットする
    public ReservationService(ReservationRepository reservationRepository, HouseRepository houseRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }
    
    @Transactional // トランザクション処理（失敗したら元に戻せる仕組み）
    public void create(Map<String, String> paymentIntentObject) {
        Reservation reservation = new Reservation(); // 新しい予約情報を作成
        
        // 支払い情報（paymentIntentObject）から宿のIDとユーザーのIDを取り出す
        Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
        Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));
        
        // 宿情報とユーザー情報をデータベースから取得する（getReferenceByIdを使う）
        House house = houseRepository.getReferenceById(houseId);
        User user = userRepository.getReferenceById(userId);

        // チェックイン日とチェックアウト日を取得（文字列を日付型に変換）
        LocalDate checkinDate = LocalDate.parse(paymentIntentObject.get("checkinDate"));
        LocalDate checkoutDate = LocalDate.parse(paymentIntentObject.get("checkoutDate"));

        // 宿泊人数と支払う料金を取得
        Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));
        Integer amount = Integer.valueOf(paymentIntentObject.get("amount"));
        
        // 予約情報をセットする
        reservation.setHouse(house);  // 宿の情報を設定
        reservation.setUser(user);  // ユーザー情報を設定
        reservation.setCheckinDate(checkinDate); // チェックイン日を設定
        reservation.setCheckoutDate(checkoutDate); // チェックアウト日を設定
        reservation.setNumberOfPeople(numberOfPeople); // 宿泊人数を設定
        reservation.setAmount(amount); // 支払う料金を設定
        
        // データベースに予約情報を保存する（reservationRepository.save()）
        reservationRepository.save(reservation);
    }
    
    // 宿泊人数が宿の定員を超えていないかチェックするメソッド
    public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {     
        return numberOfPeople <= capacity; // 宿泊人数が定員以下ならtrue（OK）
    }
    
    // 宿泊料金を計算するメソッド
    public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
        // チェックイン日からチェックアウト日までの宿泊日数を計算する
        long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate); 

        // 宿泊日数 × 1泊の料金 で合計料金を計算する
        int amount = price * (int)numberOfNights;
        return amount;
    }
}
