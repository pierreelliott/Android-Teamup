package fr.thiboud.teamup.database

import androidx.room.*
import fr.thiboud.teamup.model.User

@Dao
interface UserDao
{
    @Insert
    fun insert(user: User): Long

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * from user WHERE id = :key")
    fun get(key: Long): User?

    @Query("SELECT * from user WHERE login = :key")
    fun getByLogin(key: String): User?

    @Query("SELECT * FROM user ORDER BY id DESC LIMIT 1")
    fun getLastUser(): User?

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>?
}