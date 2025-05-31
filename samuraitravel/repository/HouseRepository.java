package com.example.samuraitravel.repository;
import java.util.List;

//モデル：リポジトリ...データベースにアクセスし、CRUD処理を行う。
//データはエンティティとしてやり取りする。
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;


public interface HouseRepository extends JpaRepository<House, Integer> {
	//JpaRepository<エンティティのクラス型, 主キーのデータ型>
	public Page<House> findByNameLike(String keyword, Pageable pageable);
	
	public Page<House> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);
    public Page<House> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);
    public Page<House> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);
    public Page<House> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);
    public Page<House> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
    public Page<House> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);
    public Page<House> findAllByOrderByCreatedAtDesc(Pageable pageable);
    public Page<House> findAllByOrderByPriceAsc(Pageable pageable);
    
    public List<House> findTop10ByOrderByCreatedAtDesc();
}
//ファインドオールを使うことでSQLを実行している、、、。
//JpaRepositoryインターフェースを継承するだけで、基本的なCRUD操作を行うためのメソッドが利用可能
//＜HOUSE＞→HOUSEテーブルをSQL文で呼び出している。
//リポジトリではOrderByキーワードを使ったメソッドを定義することで、SQLのORDER BY句と同様の並べ替えが実現できる
//ORDER BY句は、SQLでデータを並べ替えるための構文