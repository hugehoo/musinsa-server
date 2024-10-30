package com.musinsa.backend.repository

import com.musinsa.backend.domain.Brand
import com.musinsa.backend.enumerate.CategoryType
import com.musinsa.backend.domain.Product
import com.musinsa.backend.domain.QProduct.product
import com.musinsa.backend.dto.internal.BrandCategoryPrice
import com.musinsa.backend.dto.internal.BrandTotalPrice
import com.musinsa.backend.dto.internal.BrandPrice
import com.musinsa.backend.dto.response.ProductVO
import com.musinsa.backend.enumerate.PriceType
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*


@Repository
class CustomProductRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomProductRepository {

    /**
     * 1번 API
     */
    override fun findLowestPriceProductsByCategory(): Map<CategoryType, Product> {

        // 각 카테고리별 최저가격을 먼저 조회
        val lowestPrices = getLowestPriceOfCategory()

        // 각 카테고리별로 최저가격에 해당하는 상품들 중 첫 번째 상품을 선택
        // 동일 가격이 있을 경우 브랜드명 오름차순으로 정렬하여 일관성 있는 결과 제공
        return lowestPrices.map { (category, lowestPrice) ->
            category!! to getLowestPriceProductByCategory(category, lowestPrice)!!
        }.toMap()
    }

    private fun getLowestPriceProductByCategory(
        category: CategoryType?,
        lowestPrice: BigDecimal?
    ): Product? {
        return queryFactory
            .selectFrom(product)
            .join(product.brand).fetchJoin()
            .where(
                product.category.eq(category),
                product.price.eq(lowestPrice)
            )
            .orderBy(product.brand.name.asc())
            .fetchFirst()
    }

    private fun getLowestPriceOfCategory(): Map<CategoryType?, BigDecimal?> {
        val lowestPrices = queryFactory
            .select(
                product.category,
                product.price.min()
            )
            .from(product)
            .groupBy(product.category)
            .fetch()
            .associate { tuple ->
                tuple.get(product.category) to tuple.get(product.price.min())
            }
        return lowestPrices
    }

    override fun getAllProductsByBrand(brandId: Long): List<ProductVO.MinPriceByCategory> {
        // 카테고리가 겹치면 조진다. -> 최저가만 뽑아내야해.
        return queryFactory.select(
            Projections.constructor(
                ProductVO.MinPriceByCategory::class.java,
                product.category,
                product.price.min()
            )
        )
            .from(product)
            .where(product.brand.id.eq(brandId))
            .groupBy(product.category)
            .fetch()
    }


    fun getMinimumTotalPriceBrandV2(): BrandTotalPrice? {

        // 1. 모든 브랜드와 카테고리 조합의 최저가격 찾기
        val brandCategoryMinPrice = queryFactory
            .select(
                product.brand.id,
                product.category,
                product.price.min().`as`("minPrice")
            )
            .from(product)
            .groupBy(product.brand.id, product.category)
            .fetch()

        // 2. 각 브랜드별로 카테고리 최저가격 합계 계산
        val brandTotalPrices = brandCategoryMinPrice
            .groupBy { it.get(product.brand.id) }
            .mapValues { (_, prices) ->
                prices.sumOf { it as BigDecimal }  // minPrice 합계
            }

        // 3. 최저 총액을 가진 브랜드 찾기
        val minTotalBrandId = brandTotalPrices.minByOrNull { it.value }?.key
            ?: return null
// 4. 해당 브랜드의 정보와 최저가 상품들 조회
        return queryFactory
            .select(
                Projections.constructor(
                    BrandTotalPrice::class.java,
                    product.brand.id,
                    product.brand.name,
                    Expressions.constant(brandTotalPrices[minTotalBrandId])
                )
            )
            .from(product)
            .where(product.brand.id.eq(minTotalBrandId))
            .groupBy(product.brand.id, product.brand.name)
            .fetchOne()
    }

    override fun getMinimumTotalPriceBrand(): BrandTotalPrice? {

        val brandCategoryMinPrice = queryFactory
            .select(
                Projections.constructor(
                    BrandCategoryPrice::class.java,
                    product.brand.id,
                    product.category,
                    product.price.min()
                )
            )
            .from(product)
            .groupBy(product.brand.id, product.category)
            .fetch()

        println("brandCategoryMinPrice = ${brandCategoryMinPrice}")
        val brandTotalPrices: Map<Long, BigDecimal> = brandCategoryMinPrice.groupBy { it.brandId }
            .mapValues { (_, prices) -> prices.sumOf { it.totalPrice } }
        val minTotalBrandId = brandTotalPrices.entries
            .minWithOrNull(
                compareBy<Map.Entry<Long, BigDecimal>> { it.value }
                    .thenBy { it.key }  // 같은 가격일 경우 brandId 오름차순
            )?.key ?: return null

        return queryFactory
            .select(
                Projections.constructor(
                    BrandTotalPrice::class.java,
                    product.brand.id,
                    product.brand.name,
                    Expressions.constant(brandTotalPrices[minTotalBrandId])
                )
            )
            .from(product)
            .where(product.brand.id.eq(minTotalBrandId))
            .groupBy(product.brand.id, product.brand.name)
            .fetchOne()
        // original
//        return queryFactory.select(
//            Projections.constructor(
//                BrandTotalPrice::class.java,
//                product.brand.id,
//                product.brand.name,
//                product.price.sum()
//            )
//        )
//            .from(product)
//            .groupBy(product.brand.id)
//            .orderBy(product.price.sum().asc(), product.brand.name.asc())
//            .fetchFirst()
    }

    override fun findMinMaxPriceBrand(categoryType: CategoryType, priceType: PriceType): List<BrandPrice>? {
        val price = findPriceByType(categoryType, priceType)
            ?: return listOf()

        return findBrandsByPriceAndCategory(categoryType, price)
    }

    private fun findPriceByType(
        categoryType: CategoryType,
        priceType: PriceType
    ): BigDecimal? {
        val priceExpression = when (priceType) {
            PriceType.MIN -> product.price.min()
            PriceType.MAX -> product.price.max()
        }

        return queryFactory
            .select(priceExpression)
            .from(product)
            .where(product.category.eq(categoryType))
            .fetchOne()
    }

    private fun findBrandsByPriceAndCategory(
        categoryType: CategoryType,
        price: BigDecimal
    ): List<BrandPrice> {
        return queryFactory
            .select(
                Projections.constructor(
                    BrandPrice::class.java,
                    product.brand.name,
                    product.price
                )
            )
            .from(product)
            .where(
                product.category.eq(categoryType),
                product.price.eq(price)
            )
            .orderBy(product.brand.name.asc())
            .fetch()
    }

    override fun deleteAllProducts(brand: Brand) {
        queryFactory.update(product)
            .set(product.deleted, true)
            .where(product.brand.eq(brand))

    }

}